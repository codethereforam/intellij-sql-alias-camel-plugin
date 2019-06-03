package priv.thinkam.plugins;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;

/**
 * 转换例如'// abc'和'/* abc *{@literal /}'形式的单行注释为javadoc注释
 *
 * @author thinkam
 * @author Anna Bulenkova
 * @date 2019/5/30 22:46
 */
public class MainAction extends AnAction {
    /**
     * 替换用户选择的文本
     *
     * @param e AnActionEvent
     */
    @Override
    public void actionPerformed(final AnActionEvent e) {
        //Get all the required data from data keys
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        //Access document, caret, and selection
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();
        selectionModel.selectLineAtCaret();
        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();
        //Making the replacement
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, convert(selectionModel.getSelectedText(), start))
        );
        selectionModel.removeSelection();
    }

    /**
     * 编辑状态可见
     *
     * @param e AnActionEvent
     */
    @Override
    public void update(final AnActionEvent e) {
        //Get required data keys
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor
        e.getPresentation().setVisible((project != null && editor != null));
    }

    private static String convert(String originalString, int start) {
        String commentText = StringUtils.strip(StringUtils.trim(originalString), "/* ");
        return "\t/**\n" +
                "\t" + "* " + commentText + "\n" +
                "\t" + "*/\n";
    }
}
