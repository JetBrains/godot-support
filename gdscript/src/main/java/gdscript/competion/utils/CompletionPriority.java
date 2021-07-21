package gdscript.competion.utils;

public interface CompletionPriority {

    int STATIC = 50;

    int KEYWORDS = 90;
    int PRIORITY_KEYWORDS = 95;

    int USER_DEFINED = 200;

    int STATIC_TOP = 500;

}
