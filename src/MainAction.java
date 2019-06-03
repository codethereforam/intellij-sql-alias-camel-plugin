import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

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

        DynamicEngine de = DynamicEngine.getInstance();
        Object instance;
        try {
            instance = de.javaCodeToObject("MyConverter","import com.google.common.base.CaseFormat;\n" +
                    "import com.google.common.base.Splitter;\n" +
                    "import com.google.common.collect.Iterators;\n" +
                    "import com.google.common.collect.Streams;\n" +
                    "\n" +
                    "import java.util.stream.Collectors;\n" +
                    "\n" +
                    "/**\n" +
                    " * @author thinkam\n" +
                    " * @date 2019/6/2 11:56\n" +
                    " */\n" +
                    "public class MyConverter implements Converter {\n" +
                    "    @Override\n" +
                    "    public String convert(String origin)  {\n" +
                    "        return Streams.stream(Splitter.on(\",\").trimResults()\n" +
                    "                .omitEmptyStrings()\n" +
                    "                .split(origin))\n" +
                    "                .map(s -> {\n" +
                    "                    if (s.contains(\" \")) {\n" +
                    "                        return s;\n" +
                    "                    }\n" +
                    "                    return s + \" \" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,\n" +
                    "                            Iterators.getLast(Splitter.on(\".\").split(s).iterator()));\n" +
                    "                })\n" +
                    "                .collect(Collectors.joining(\", \"));\n" +
                    "    }\n" +
                    "}");
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        Converter converter = (Converter) instance;
        //Making the replacement
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, converter.convert(selectionModel.getSelectedText()))
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
}
