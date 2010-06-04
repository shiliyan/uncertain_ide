package uncertain.ide.eclipse.editor.textpage;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;

/**
 * ����Undo���������ڼ����ı�����ı��ı��¼�������Undo��������¼��<br>
 * 
 * @author qujinlong
 */
public class UndoManager
{
  /*
   * ���ڴ洢��ʷUndo������ÿ�ı�һ���ı����ݣ��ͽ�����һ��Undo��������OperationHistory�С�
   */
  private final IOperationHistory opHistory;

  /*
   * Undo���������ģ�һ��������OperationHistory�в��ҵ�ǰ�ı����Undo������
   */
  private IUndoContext undoContext = null;

  /*
   * ��Ҫ��������Ҫʵ��Undo�������ı���
   */
  private StyledText styledText = null;

  private int undoLevel = 0;

  public UndoManager(int undoLevel)
  {
    opHistory = OperationHistoryFactory.getOperationHistory();

    setMaxUndoLevel(undoLevel);
  }

  public void setMaxUndoLevel(int undoLevel)
  {
    this.undoLevel = Math.max(0, undoLevel);

    if (isConnected())
      opHistory.setLimit(undoContext, this.undoLevel);
  }

  public boolean isConnected()
  {
    return styledText != null;
  }

  /*
   * ��Undo��������ָ����StyledText�ı����������
   */
  public void connect(StyledText styledText)
  {
    if (! isConnected() && styledText != null)
    {
      this.styledText = styledText;

      if (undoContext == null)
        undoContext = new ObjectUndoContext(this);

      opHistory.setLimit(undoContext, undoLevel);
      opHistory.dispose(undoContext, true, true, false);

      addListeners();
    }
  }

  public void disconnect()
  {
    if (isConnected())
    {
      removeListeners();

      styledText = null;

      opHistory.dispose(undoContext, true, true, true);

      undoContext = null;
    }
  }

  private ExtendedModifyListener extendedModifyListener = null;

  private boolean isUndoing = false;

  /*
   * ��Styled��ע������ı��ı�ļ�������
   * 
   * ����ı��ı䣬�͹���һ��Undo����ѹ��Undo����ջ�С�
   */
  private void addListeners()
  {
    if (styledText != null)
    {
      extendedModifyListener = new ExtendedModifyListener() {
        public void modifyText(ExtendedModifyEvent event)
        {
          if (isUndoing)
            return;

          String newText = styledText.getText().substring(event.start,
              event.start + event.length);

          UndoableOperation operation = new UndoableOperation(undoContext);

          operation.set(event.start, newText, event.replacedText);

          opHistory.add(operation);
        }
      };

      styledText.addExtendedModifyListener(extendedModifyListener);
    }
  }

  private void removeListeners()
  {
    if (styledText != null)
    {
      if (extendedModifyListener != null)
      {
        styledText.removeExtendedModifyListener(extendedModifyListener);

        extendedModifyListener = null;
      }
    }
  }

  public void redo()
  {
    if (isConnected())
    {
      try
      {
        opHistory.redo(undoContext, null, null);
      }
      catch (ExecutionException ex)
      {
      }
    }
  }

  public void undo()
  {
    if (isConnected())
    {
      try
      {
        opHistory.undo(undoContext, null, null);
      }
      catch (ExecutionException ex)
      {
      }
    }
  }

  /*
   * Undo�������ڼ�¼StyledText���ı����ı�ʱ��������ݡ�
   * 
   * �����ı����б������ı�Ϊ111222333�������ʱѡ��222�滻Ϊ444���ø���ճ���ķ�������
   * 
   * ��Undo�����м�¼���������Ϊ�� startIndex = 3; newText = 444; replacedText = 222;
   */
  private class UndoableOperation extends AbstractOperation
  {
    // ��¼Undo����ʱ,���滻�ı��Ŀ�ʼ����
    protected int startIndex = - 1;

    // ��������ı�
    protected String newText = null;

    // ���滻�����ı�
    protected String replacedText = null;

    public UndoableOperation(IUndoContext context)
    {
      super("Undo-Redo");

      addContext(context);
    }

    /*
     * ����Undo������Ҫ�洢��������ݡ�
     */
    public void set(int startIndex, String newText, String replacedText)
    {
      this.startIndex = startIndex;

      this.newText = newText;
      this.replacedText = replacedText;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.operations.AbstractOperation#undo(org.eclipse.core.runtime.IProgressMonitor,
     *      org.eclipse.core.runtime.IAdaptable)
     */
    public IStatus undo(IProgressMonitor monitor, IAdaptable info)
        throws ExecutionException
    {
      isUndoing = true;
      styledText.replaceTextRange(startIndex, newText.length(), replacedText);
      isUndoing = false;

      return Status.OK_STATUS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.operations.AbstractOperation#redo(org.eclipse.core.runtime.IProgressMonitor,
     *      org.eclipse.core.runtime.IAdaptable)
     */
    public IStatus redo(IProgressMonitor monitor, IAdaptable info)
        throws ExecutionException
    {
      isUndoing = true;
      styledText.replaceTextRange(startIndex, replacedText.length(), newText);
      isUndoing = false;

      return Status.OK_STATUS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor,
     *      org.eclipse.core.runtime.IAdaptable)
     */
    public IStatus execute(IProgressMonitor monitor, IAdaptable info)
        throws ExecutionException
    {
      return Status.OK_STATUS;
    }
  }
}

