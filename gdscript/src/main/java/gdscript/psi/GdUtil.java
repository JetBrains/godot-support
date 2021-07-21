package gdscript.psi;

public class GdUtil {

    // TODO
//    public static List<GdConstDecl> findConstants(VirtualFile _file) {
////        public static List<SimpleProperty> findProperties (Project project, String key) {
//        List<GdConstDecl> result = new ArrayList<>();
//        //GdFile file = (GdFile) PsiManager.getInstance(GdLanguage.INSTANCE).findFile(virtualFile);
//
//
////        Collection<VirtualFile> virtualFiles =
////                FileTypeIndex.getFiles(SimpleFileType.INSTANCE, GlobalSearchScope.allScope(project));
////        for (VirtualFile virtualFile : virtualFiles) {
////            SimpleFile simpleFile = (SimpleFile) PsiManager.getInstance(project).findFile(virtualFile);
////            if (simpleFile != null) {
////                SimpleProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, SimpleProperty.class);
////                if (properties != null) {
////                    for (SimpleProperty property : properties) {
////                        if (key.equals(property.getKey())) {
////                            result.add(property);
////                        }
////                    }
////                }
////            }
////        }
//
//        return result;
//    }

//    /**
//     * Attempts to collect any comment elements above the Simple key/value pair.
//     */
//    public static @NotNull String findDocumentationComment(SimpleProperty property) {
//        List<String> result = new LinkedList<>();
//        PsiElement element = property.getPrevSibling();
//        while (element instanceof PsiComment || element instanceof PsiWhiteSpace) {
//            if (element instanceof PsiComment) {
//                String commentText = element.getText().replaceFirst("[!# ]+", "");
//                result.add(commentText);
//            }
//            element = element.getPrevSibling();
//        }
//        return StringUtil.join(Lists.reverse(result),"\n ");
//    }

}
