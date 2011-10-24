package aurora.ide.editor.textpage;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class HoverInformationControl extends BrowserInformationControl {
    private String symbolicFontName;

    public HoverInformationControl(Shell parent, String symbolicFontName, boolean resizable) {
        super(parent, symbolicFontName, resizable);
        this.symbolicFontName = symbolicFontName;
    }

    public HoverInformationControl(Shell parent, String symbolicFontName, String statusFieldText) {
        super(parent, symbolicFontName, statusFieldText);
        this.symbolicFontName = symbolicFontName;
    }

    public HoverInformationControl(Shell parent, String symbolicFontName, ToolBarManager toolBarManager) {
        super(parent, symbolicFontName, toolBarManager);
        this.symbolicFontName = symbolicFontName;
    }

    public IInformationControlCreator getInformationPresenterControlCreator() {
        return new IInformationControlCreator() {
            public IInformationControl createInformationControl(Shell parent) {
                if (HoverInformationControl.isAvailable(parent))
                    return new HoverInformationControl(parent, symbolicFontName, true);
                return new DefaultInformationControl(parent);
            }
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setInformation(String content) {
        if (!content.startsWith("<html>")) {
            content = TextHover.html(content);
        }
        super.setInformation(content);
    }

}
