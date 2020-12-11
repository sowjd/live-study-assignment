import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.*;

public class Dashboard {
    private static final String PERSONAL_TOKEN = "WRITE_YOUR_PERSONAL_TOKEN_HERE";
    private static final String REPOSITORY = "whiteship/live-study";
    public static final int[] ISSUE_ID = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    private static HashMap<String, WeekInfo> weekInfoPerUser = new HashMap<String, WeekInfo>();
    private static int[] weeklySubmittedUserCount = new int[ISSUE_ID.length];

    public static void main(String[] args) throws IOException {
        getDashboardDataFromGithub();
        printDashboard();
    }

    public static void getDashboardDataFromGithub() throws IOException {
        // 깃허브 연결
        GitHub github = new GitHubBuilder().withOAuthToken(PERSONAL_TOKEN).build();

        // 이슈에 커멘트를 단 유저의 아이디와 그 수를 취득
        for (int i = 0; i < ISSUE_ID.length; i++) {
            GHIssue issue = github.getRepository(REPOSITORY).getIssue(ISSUE_ID[i]);
            List<GHIssueComment> allComments = issue.getComments();

            if (allComments == null) continue;

            HashSet<String> submittedUser = new HashSet<String>();

            for (GHIssueComment comment : allComments) {
                String userID = comment.getUser().getLogin();
                submittedUser.add(userID);
                if (weekInfoPerUser.containsKey(userID)) weekInfoPerUser.get(userID).submit(i);
                else weekInfoPerUser.put(userID, new WeekInfo(i));
            }

            weeklySubmittedUserCount[i] = submittedUser.size();
        }
    }

    public static void printDashboard() {
        String title = "| 참여자 |";
        String dash = "| --- |";
        for (int i = 0; i < ISSUE_ID.length; i++) {
            title += " " + (i + 1) + "주차<br>" + weeklySubmittedUserCount[i] + "/" + weekInfoPerUser.size() + "<br>";
            String submitRatio = String.format("%.2f", (float) weeklySubmittedUserCount[i] / weekInfoPerUser.size() * 100) + "%";
            title += "(" + submitRatio + ") |";
            dash += " :---: |";
        }
        title += " 참석율 |";
        dash += " --- |";
        System.out.println(title);
        System.out.println(dash);

        SortedSet<String> sortedUserID = new TreeSet<String>(weekInfoPerUser.keySet());
        for (String userID : sortedUserID) {
            String row = "| " + userID + " |";

            for (boolean submission : weekInfoPerUser.get(userID).getWeek()) {
                if (submission) row += ":white_check_mark:";
                row += "|";
            }
            row += " " + weekInfoPerUser.get(userID).getSubmitRatio() + " |";
            System.out.println(row);
        }
    }
}