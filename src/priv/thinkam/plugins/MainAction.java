package priv.thinkam.plugins;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;
import com.google.common.collect.Streams;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

import java.util.stream.Collectors;

/**
 * 给SQL查询语句字段添加驼峰别名
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

        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();
        //Making the replacement
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, convert(selectionModel.getSelectedText()))
        );
        selectionModel.removeSelection();
    }

    /**
     * 当有文本被选中时右键才显示改插件的菜单
     *
     * @param e AnActionEvent
     */
    @Override
    public void update(final AnActionEvent e) {
        //Get required data keys
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor and if some text in the editor is selected
        e.getPresentation().setVisible((project != null && editor != null && editor.getSelectionModel().hasSelection()));
    }

    /**
     * 字符串转换(e.g. “user_name, b.user_age” 被转换成 "user_name userName, b.user_age userAge”)
     *
     * @param originString 原来的文本
     * @return 转换后的字符串
     * @author thinkam
     * @date 2019/5/30 22:48
     */
    private static String convert(String originString) {
        return Streams.stream(Splitter.on(",").trimResults()
                .omitEmptyStrings()
                .split(originString))
                .map(s -> s + " " + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
                        Iterators.getLast(Splitter.on(".").split(s).iterator())))
                .collect(Collectors.joining(", "));
    }
}
