package hanu.example.corespringsecurity.utill.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

// 해당 포멧터는 초기 시동시 동작하는 DDL에는 적용되지 않는다.
public class CustomP6spySqlFormat implements MessageFormattingStrategy {
    // 표기에 허용되지 않는 filter
    private List<String> DENIED_FILTER = Arrays.asList("Test1"
            , this.getClass().getSimpleName());

    // 표기에 허용되는 filter
    private final String ALLOW_FILTER = "hanu.example";

//    @Override
//    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
//        sql = formatSql(category, sql);
//        return now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + sql;
//    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        if(sql.trim().isEmpty()) { // sql 이 없다면 출력하지 않아도 됨
            return "";
        }
        // stack 을 구성하는 Format을 만든다
        return sql + createStack(connectionId, elapsed);
    }

    private String formatSql(String category,String sql) {
        if(sql ==null || sql.trim().equals("")) return sql;

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if(tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            }else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
        }

        return sql;
    }

    // stack 콘솔 표기
    private String createStack(int connectionId, long elapsed) {
        Stack<String> callStack = new Stack<>();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        for (StackTraceElement stackTraceElement : stackTrace) {
            String trace = stackTraceElement.toString();

            // trace 항목을 보고 내게 맞는 것만 필터
//            if(trace.startsWith(ALLOW_FILTER) && !filterDenied(trace)) {
//                callStack.push(trace);
//            }

            if(trace.startsWith(ALLOW_FILTER)) {
                callStack.push(trace);
            }

        }

        StringBuffer sb = new StringBuffer();
        int order = 1;
        while (callStack.size() != 0) {
            sb.append("\n\t\t" + (order++) + "." + callStack.pop());
        }

        return new StringBuffer().append("\n\n\tConnection ID:").append(connectionId)
                .append(" | Excution Time:").append(elapsed).append(" ms\n")
                .append("\n\tExcution Time:").append(elapsed).append(" ms\n")
                .append("\n\tCall Stack :").append(sb).append("\n")
                .append("\n--------------------------------------")
                .toString();
    }
}