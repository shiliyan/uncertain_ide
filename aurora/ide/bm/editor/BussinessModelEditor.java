package aurora.ide.bm.editor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.ui.PartInitException;

import uncertain.composite.CompositeMap;
import aurora.ide.editor.BaseCompositeMapEditor;
import aurora.ide.editor.CompositeMapPage;
import aurora.ide.helpers.ApplicationException;
import aurora.ide.helpers.AuroraConstant;
import aurora.ide.helpers.CompositeMapLocatorParser;
import aurora.ide.helpers.CompositeMapUtil;
import aurora.ide.helpers.DialogUtil;
import aurora.ide.helpers.LocaleMessage;
import aurora.ide.preferencepages.CustomSettingPreferencePage;

public class BussinessModelEditor extends BaseCompositeMapEditor {

    protected BussinessModelPage mainFormPage;
    private SQLExecutePage       sqlPage    = new SQLExecutePage(this);
    private ViewSource           viewSource = new ViewSource(this);
    int                          SQLPageIndex;
    private int                  viewSourceIndex;

    public CompositeMapPage initMainViewerPage() {
        mainFormPage = new BussinessModelPage(this);
        return mainFormPage;
    }

    protected void addPages() {
        try {
            super.addPages();
            SQLPageIndex = addPage(sqlPage);
            viewSourceIndex = addPage(viewSource, getEditorInput());
            this.setPageText(viewSourceIndex, LocaleMessage.getString("view.source"));
            setActivePage(CustomSettingPreferencePage.getBMEditorInitPageIndex());
        } catch (PartInitException e) {
            DialogUtil.showExceptionMessageBox(e);
        }
    }

    public void editorDirtyStateChanged() {
        super.editorDirtyStateChanged();
        sqlPage.setModify(true);
    }

    protected void pageChange(int newPageIndex) {
        int currentPage = getCurrentPage();
        super.pageChange(newPageIndex);
        if (newPageIndex == SQLPageIndex) {
            try {
                sqlPage.refresh(CompositeMapUtil.getFullContent(mainFormPage.getData()));
            } catch (ApplicationException e) {
                DialogUtil.showExceptionMessageBox(e);
            }
        } else if (newPageIndex == viewSourceIndex) {
            viewSource.refresh();
        } else if (currentPage == mainViewerIndex && newPageIndex == textPageIndex
                && mainViewerPage.isFormContendCreated()) {
            try {
                locateTextPage();
            } catch (ApplicationException e) {
                DialogUtil.showExceptionMessageBox(e);
            }
        } else if (currentPage == textPageIndex && newPageIndex == mainViewerIndex && textPage.checkContentFormat()) {
            locateMainPage();
        }
    }

    private void locateMainPage() {
        CompositeMapLocatorParser parser = new CompositeMapLocatorParser();

        try {
            InputStream content = new ByteArrayInputStream(textPage.getContent().getBytes("UTF-8"));
            CompositeMap cm = parser.getCompositeMapFromLine(content, textPage.getCursorLine());
            if (cm != null) {
                while (cm.getParent() != null) {
                    CompositeMap parent = cm.getParent();
                    if (AuroraConstant.ModelQN.equals(parent.getQName())) {
                        mainFormPage.setSelectionTab(cm.getName());
                    }
                    cm = parent;
                }
            }
        } catch (Exception e) {
            DialogUtil.showExceptionMessageBox(e);
        }

    }

    private void locateTextPage() throws ApplicationException {
        CompositeMap selection = mainFormPage.getSelectionTab();
        if (selection == null)
            return;
        int line = 0;
        line = CompositeMapUtil.locateNode(CompositeMapUtil.getFullContent(mainFormPage.getData()), selection);
        int offset = textPage.getOffsetFromLine(line);
        textPage.setHighlightRange(offset, 10, true);

    }
}
