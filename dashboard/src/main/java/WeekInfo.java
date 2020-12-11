public class WeekInfo {
    private int TOTAL_WEEK = Dashboard.ISSUE_ID.length;
    private boolean[] week = new boolean[TOTAL_WEEK];
    private int submitCount = 0;

    WeekInfo(int weekIdx) {
        submit(weekIdx);
    }

    public boolean[] getWeek() {
        return week;
    }

    public void submit(int weekIdx) {
        if (!week[weekIdx]) {
            week[weekIdx] = true;
            submitCount++;
        }
    }

    public String getSubmitRatio() {
        return String.format("%.2f", (float) submitCount / TOTAL_WEEK * 100) + "%";
    }
}
