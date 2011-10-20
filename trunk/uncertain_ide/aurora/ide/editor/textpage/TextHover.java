package aurora.ide.editor.textpage;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.ISourceViewerExtension2;

import uncertain.composite.CompositeMap;
import uncertain.composite.IterationHandle;
import uncertain.composite.QualifiedName;
import uncertain.schema.Attribute;
import uncertain.schema.Element;
import uncertain.schema.IType;
import aurora.ide.editor.textpage.scanners.XMLTagScanner;
import aurora.ide.helpers.CompositeMapUtil;
import aurora.ide.helpers.LoadSchemaManager;
import aurora.ide.search.core.Util;

public class TextHover extends DefaultTextHover implements ITextHoverExtension {
    private ISourceViewer sourceViewer;
    private String        style = "<style> body,table{ font-family:sans-serif; font-size:9pt; background:#FFFFE1; } </style>";

    public TextHover(ISourceViewer sourceViewer) {
        super(sourceViewer);
        this.sourceViewer = sourceViewer;
    }

    public IInformationControlCreator getHoverControlCreator() {
        return new HoverInformationControlCreator();
    }

    @Override
    public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
        String hover = getMarkerInfo(sourceViewer, hoverRegion);
        if (hover != null)
            return html(hover);

        try {
            IDocument doc = textViewer.getDocument();
            final int line = doc.getLineOfOffset(hoverRegion.getOffset());
            final String[] word = { doc.get(hoverRegion.getOffset(), hoverRegion.getLength()) };
            if (word[0] == null || word[0].trim().length() == 0)
                return null;
            if (isAttributeValue(doc, line, hoverRegion.getOffset(), hoverRegion.getLength()))
                return html(word[0]);
            CompositeMap map = CompositeMapUtil.loaderFromString(doc.get());
            map.iterate(new IterationHandle() {

                public int process(CompositeMap map) {
                    if (map.getLocation().getStartLine() == line + 1) {
                        Element ele = LoadSchemaManager.getSchemaManager().getElement(map);
                        if (ele != null) {
                            @SuppressWarnings("unchecked")
                            List<Attribute> list = ele.getAllAttributes();
                            if (list != null) {
                                StringBuilder sb = new StringBuilder(2000);
                                sb.append("Defined Attributes in [ ");
                                sb.append(map.getName());
                                sb.append(" ]:<hr/><table>");
                                for (Attribute a : list) {
                                    if (word[0].equalsIgnoreCase(a.getName())) {
                                        word[0] = a.getName() + "<br/>" + notNull(a.getDocument()) + "<br/>Type : "
                                                + getTypeNameNotNull(a.getAttributeType());
                                        return IterationHandle.IT_BREAK;
                                    }
                                    sb.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", a.getName(),
                                            notNull(a.getDocument()), getTypeNameNotNull(a.getAttributeType())));
                                }
                                sb.append("</table>");
                                if (word[0].equalsIgnoreCase(map.getName()))
                                    word[0] = sb.toString();
                            }
                        }
                        return IterationHandle.IT_BREAK;
                    }
                    return 0;
                }
            }, true);
            return html(word[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getTypeNameNotNull(IType type) {
        if (type == null)
            return "";
        QualifiedName qfn = type.getQName();
        if (qfn == null)
            return "";
        String name = qfn.getLocalName();
        if (name == null)
            return "";
        return name;
    }

    private String notNull(String str) {
        return str == null ? "" : str;
    }

    @Override
    public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
        IDocument doc = textViewer.getDocument();
        try {
            final int line = doc.getLineOfOffset(offset);
            int ls = doc.getLineOffset(line);
            int len = doc.getLineLength(line);

            String text = doc.get(ls, len);
            if (text == null || text.length() == 0)
                return super.getHoverRegion(textViewer, offset);
            int s = offset - ls, e = offset - ls;
            char c = text.charAt(s);
            if (isWordPart(c)) {
                while (s >= 0 && isWordPart(text.charAt(s)))
                    s--;
                s++;
                while (e < text.length() && isWordPart(text.charAt(e)))
                    e++;
                return new Region(ls + s, e - s);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return super.getHoverRegion(textViewer, offset);
    }

    private String getMarkerInfo(ISourceViewer sourceViewer, IRegion hoverRegion) {
        IAnnotationModel model = null;
        if (sourceViewer instanceof ISourceViewerExtension2) {
            ISourceViewerExtension2 extension = (ISourceViewerExtension2) sourceViewer;
            model = extension.getVisualAnnotationModel();
        } else
            model = sourceViewer.getAnnotationModel();
        if (model == null)
            return null;

        @SuppressWarnings("unchecked")
        Iterator<Annotation> e = model.getAnnotationIterator();
        while (e.hasNext()) {
            Annotation a = e.next();
            String type = a.getType();
            if (!XmlErrorReconcile.AnnotationType.equals(type)
                    && !"org.eclipse.ui.workbench.texteditor.warning".equals(type)
                    && !"org.eclipse.ui.workbench.texteditor.error".equals(type))
                continue;
            Position p = model.getPosition(a);
            if (p != null && p.overlapsWith(hoverRegion.getOffset(), hoverRegion.getLength())) {
                String msg = a.getText();
                if (msg != null && msg.trim().length() > 0)
                    return msg;
            }
        }

        return null;
    }

    private String html(String str) {
        StringBuilder sb = new StringBuilder(2000);
        sb.append("<html><head>");
        sb.append(style);
        sb.append("</head><body>");
        sb.append(str);
        sb.append("</body></html>");
        return sb.toString();
    }

    private boolean isAttributeValue(IDocument doc, int line, int offset, int length) {
        try {
            XMLTagScanner scanner = Util.getXMLTagScanner();
            int lineoffset = doc.getLineOffset(line);
            int linelength = doc.getLineLength(line);
            scanner.setRange(doc, lineoffset, linelength);
            IToken token = Token.EOF;
            while ((token = scanner.nextToken()) != Token.EOF) {
                if (token.getData() instanceof TextAttribute) {
                    TextAttribute text = (TextAttribute) token.getData();
                    if (new Position(scanner.getTokenOffset(), scanner.getTokenLength()).overlapsWith(offset, length)) {
                        if (text.getForeground().getRGB().equals(IColorConstants.STRING)) {
                            return true;
                        }
                        break;
                    }
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isWordPart(char c) {
        return Character.isJavaIdentifierPart(c) || c == '-';
    }

}
