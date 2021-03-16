package com.mrcd.code.checker;

import com.puppycrawl.tools.checkstyle.api.*;

/**
 * Android，Java代码，检查RecyclerViewHolder有没有类注释，类注释上是否关联了布局文件
 */
public class RecyclerViewHolderChecker extends Check {

    /**
     * Android下RecyclerView的ViewHolder类名
     */
    private static final String RECYCLER_VIEW_HOLDER_CLASS_NAME = "RecyclerViewHolder";

    /**
     * link的关键字
     */
    private static final String LINK_KEY_WORDS = "@link";
    /**
     * 注释中的关键字，如果没有这个关键字的话，那么就不通过
     */
    private static final String LAYOUT_KEY_WORDS = "R.layout#";
    private static final String ERROR_FORMAT = "未在类注释中发现ViewHolder的关联布局";

    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.CLASS_DEF};
    }

    @Override
    public void visitToken(DetailAST aAST) {
        DetailAST extendToken = aAST.findFirstToken(TokenTypes.EXTENDS_CLAUSE);
        if (extendToken != null) {
            DetailAST token = extendToken.getFirstChild();
            if (null != token) {
                String className = token.getText();
                if (RECYCLER_VIEW_HOLDER_CLASS_NAME.equals(className)) {
                    checkDoc(aAST, getFileContents());
                }
            }
        }
    }

    private void checkDoc(DetailAST aAST, FileContents fileContents) {
        TextBlock cmt = fileContents.getJavadocBefore(aAST.getLineNo());
        if (null == cmt) {
            log(aAST.getLineNo(), ERROR_FORMAT);
            return;
        }
        String[] text = cmt.getText();
        if (text == null || text.length <= 0) {
            log(aAST.getLineNo(), ERROR_FORMAT);
        } else {
            boolean isCorrect = false;
            for (String item : text) {
                if (null != item && item.contains(LINK_KEY_WORDS) && item.contains(LAYOUT_KEY_WORDS)) {
                    isCorrect = true;
                    break;
                }
            }
            if (!isCorrect) {
                log(aAST.getLineNo(), ERROR_FORMAT);
            }
        }
    }
}
