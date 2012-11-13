package aurora.ide.preferencepages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.swt.graphics.Image;

import aurora.ide.AuroraPlugin;
import aurora.ide.helpers.DialogUtil;
import aurora.ide.helpers.ImagesUtils;
import aurora.ide.helpers.LocaleMessage;
import aurora.ide.helpers.SystemException;

public class AuroraTemplateAssistProcessor extends TemplateCompletionProcessor {
	protected String extractPrefix(ITextViewer viewer, int offset) {
		int i = offset;
		IDocument document = viewer.getDocument();
		if (i > document.getLength())
			return "";
		try {
			while (i > 0) {
				char ch = document.getChar(i - 1);
				if (!Character.isJavaIdentifierPart(ch))
					break;
				i--;
			}
			if (i > 0) {
				int j = i;
				if (document.getChar(j - 1) == '<')
					i--;
			}
			return document.get(i, offset - i);
		} catch (BadLocationException e) {
			return "";
		}
	}

	protected Template[] getTemplates(String contextTypeId) {
		AuroraTemplateManager manager = AuroraTemplateManager.getInstance();
		try {
			return manager.getTemplateStore().getTemplates();
		} catch (SystemException e) {
			DialogUtil.showExceptionMessageBox(e);
			return null;
		}
	}

	protected TemplateContextType getContextType(ITextViewer viewer,
			IRegion region) {
		AuroraTemplateManager manager = AuroraTemplateManager.getInstance();
		return manager.getContextTypeRegistry().getContextType(
				AuroraTemplateContextType.new_screen);
	}

	protected Image getImage(Template template) {
		// return
		// Activator.getDefault().getImageRegistry().get(Activator.ICON_TEMPLATE);
		// return AuroraPlugin.getImageDescriptor(
		// LocaleMessage.getString("category.icon")).createImage();
		return ImagesUtils.getImage("category.gif");
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		ITextSelection selection = (ITextSelection) viewer
				.getSelectionProvider().getSelection();
		// adjust offset to end of normalized selection
		if (selection.getOffset() == offset)
			offset = selection.getOffset() + selection.getLength();
		String prefix = extractPrefix(viewer, offset);
		Region region = new Region(offset - prefix.length(), prefix.length());
		TemplateContext context = createContext(viewer, region);
		if (context == null)
			return new ICompletionProposal[0];
		context.setVariable("selection", selection.getText()); // name of the selection variables {line, word_selection //$NON-NLS-1$
		Template[] templates = getTemplates(context.getContextType().getId());
		List matches = new ArrayList();
		for (int i = 0; i < templates.length; i++) {
			Template template = templates[i];
			try {
				context.getContextType().validate(template.getPattern());
			} catch (TemplateException e) {
				continue;
			}
			if (!prefix.equals("") && prefix.charAt(0) == '<')
				prefix = prefix.substring(1);
			if (!prefix.equals("")
					&& (template.getName().startsWith(prefix) && template
							.matches(prefix, context.getContextType().getId())))
				matches.add(createProposal(template, context, (IRegion) region,
						getRelevance(template, prefix)));
		}
		return (ICompletionProposal[]) matches
				.toArray(new ICompletionProposal[matches.size()]);
	}

}