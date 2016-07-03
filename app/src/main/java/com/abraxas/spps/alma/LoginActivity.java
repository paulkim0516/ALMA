package com.abraxas.spps.alma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;


public class
LoginActivity extends AppCompatActivity {

    public static String SCHOOL_CLASS= "com.spps.alma.schoolClass";
    public static String SEMESTER = "com.spps.alma.semester";
    public static String COOKIE = "com.spps.alma.cookie";
    public static String CALENDAR = "com.spps.alma.calendar";
    public ArrayList<Course> schoolClass = new ArrayList<>();
    private String cookie = "";
    private UserLoginTask mAuthTask = null;

    public HttpsURLConnection https = null;

    private String[] userInfo = {"foo", "bar"};

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    int status = 0;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.email);
        mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_NEXT) {
                    View focusView = LoginActivity.this.mPasswordView;
                    focusView.requestFocus();
                    return true;
                }
                return false;
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        assert mEmailSignInButton != null;
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        boolean autoLogin = getIntent().getBooleanExtra(LaunchActivity.AUTOLOGIN, false);
        //TODO delete this before releasing
        if(autoLogin){
            mAuthTask = new UserLoginTask(userInfo[0], userInfo[1]);
            showProgress(true);
            startTime = System.currentTimeMillis();
            mAuthTask.execute();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        String email = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mUsernameView.getWindowToken(),0);
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            startTime = System.currentTimeMillis();
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        private final String url = "https://spps.getalma.com/";
        private int errorCount = 0;
        boolean connected = true;
        private URLConnection urlConnection = null;
        private InputStream inputStream = null;
        private OutputStream outputStream = null;
        private String[][] requestProperties = null;
        private StringBuffer response = new StringBuffer();
        private String currentSem="";
        private Attendance attendance = new Attendance();
        private SchoolCalendar schoolCalendar = new SchoolCalendar();
        HashMap<String, String> classIds = new HashMap<>();
        HashMap<String, String> semIds = new HashMap<>();
        ArrayList<Assignment> alist;
        ArrayList<Category> clist;


        public int semester;
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        protected Boolean doInBackground(Void... params) {
            ConnectivityManager cm =
                    (ConnectivityManager)LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            connected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if(!connected){
                return false;
            }


            String payload = "{\"username\":\""+mEmail+"\",\"password\":\""+mPassword+"\"}";
            try {
                urlConnection = new URL(url+"login").openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            requestProperties = new String[][]{
                    {"Accept", "*/*"},
                    {"Accept-Encoding", "gzip, deflate, sdch"},
                    {"Accept-Language", "en-US,en;q=0.8,ko;q=0.6"},
                    {"Connection", "keep-alive"},
                    {"Content-Length", Integer.toString(payload.length())},
                    {"Content-Type", "application/json"},
                    {"Host", "spps.getalma.com"},
                    {"Origin", "https://spps.getalma.com"},
                    {"Referer", "https://spps.getalma.com"},
                    {"User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36"},
                    {"X-Requested-With", "XMLHttpRequest"}
            };
            https = (HttpsURLConnection) urlConnection;
            https.setDoOutput(true);
            setRequestProperties(requestProperties);
            try {
                https.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            try {
                outputStream = https.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.write(payload.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                https.connect();
                status = https.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("tag", status+"");
            if(status == 200){
                try {
                    cookie = https.getHeaderField("Set-Cookie").split(";")[0];
                    inputStream = https.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] buffer;
                try {
                    buffer = new byte[2];
                    GZIPInputStream gis = new GZIPInputStream(inputStream);
                    gis.skip(13);

                    gis.read(buffer, 0, 1);
                    if(buffer[0]==50){
                        gis.close();
                        inputStream.close();
                        outputStream.close();

                        getMainContent();
                        return true;
                    }
                    gis.skip(51);
                    gis.read(buffer,1,1);
                    if(buffer[1]<58){
                        gis.close();
                        inputStream.close();
                        outputStream.close();
                        errorCount = buffer[1]-48;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            https.disconnect();
            return false;
        }

        int classNum=0;

        @SuppressLint("DefaultLocale")
        private void getMainContent() throws IOException {

            String[] container2;
            String[] classContainer;
            Name teacherName;

            urlConnection = new URL(url + "home").openConnection();
            requestProperties = new String[][]{
                    {"Host", "spps.getalma.com"},
                    {"Connection", "keep-alive"},
                    {"Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"},
                    {"Upgrade-Insecure-Requests", "1"},
                    {"User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36"},
                    {"Accept-Encoding", "gzip, deflate, sdch"},
                    {"Accept-Language", "en-US,en;q=0.8,ko;q=0.6"},
                    {"Cookie", cookie}
            };
            commonConnection(requestProperties);
            String studentId= response.toString().split("Keen.setGlobalProperties")[1].split("id: \"")[1].split("\"")[0];

            urlConnection = new URL(url +"home/grades").openConnection();
            commonConnection(requestProperties);
            container2 = response.toString().split("</select>");

            for(int i=0;i<container2[0].split("period\">")[1].split("</option>").length-1;i++){
                Log.d("tag", Html.fromHtml(container2[0].split("period\">")[1].split("</option>")[i].split(">")[1]).toString() +" : "+ container2[0].split("period\">")[1].split("</option>")[i].split("value=\"")[1].split("\"")[0]);
                semIds.put(Html.fromHtml(container2[0].split("period\">")[1].split("</option>")[i].split(">")[1]).toString(), container2[0].split("period\">")[1].split("</option>")[i].split("value=\"")[1].split("\"")[0]);
            }

            String classContainer1="", classContainer2;
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            boolean check = true;
            while(check){
                urlConnection = new URL(url+"home/get-student-schedule?studentId="+studentId+"&date="+c.get(Calendar.YEAR)+"-"+String.format("%02d",c.get(Calendar.MONTH)+1)+"-"+String.format("%02d",c.get(Calendar.DATE))).openConnection();
                commonConnection(requestProperties);
                classContainer1=response.toString().split("html\":\"")[1].split("\"\\}")[0].replaceAll("u00", "!.").replaceAll("!.22", "\"").replaceAll("!.3C", "<").replaceAll("!.3E",">").replaceAll("!.26","&").replaceAll("\\\\n","").replaceAll(" {2,}","").replaceAll("\\\\","");

                if(classContainer1.contains("No classes scheduled today.")){
                    c.add(Calendar.WEEK_OF_YEAR,1);
                }else{
                    check=false;
                }
            }
            c.add(Calendar.DATE,1);

            urlConnection = new URL(url+"home/get-student-schedule?studentId="+studentId+"&date="+c.get(Calendar.YEAR)+"-"+String.format("%02d", c.get(Calendar.MONTH) + 1)+"-"+String.format("%02d",c.get(Calendar.DATE))).openConnection();
            commonConnection(requestProperties);

            classContainer2=response.toString().split("html\":\"")[1].split("\"\\}")[0].replaceAll("u00", "!.").replaceAll("!.22","\"").replaceAll("!.3C","<").replaceAll("!.3E",">").replaceAll("!.26","&").replaceAll(" {2,}","").replaceAll("\\\\n","").replaceAll("\\\\","");

            classContainer = classContainer1.split("</tbody>")[0].concat(classContainer2.split("</tbody>")[0]).split("</tr>");

            classNum = classContainer.length-1;


            for(int i=0;i<classNum;i++){
                classIds.put(container2[1].split("classId\">")[1].split("</option>")[i].split(">")[1], container2[1].split("</option>")[i].split("value=\"")[1].split("\"")[0]);
                schoolClass.add(i,new Course());
            }

            currentSem = container2[0].split("selected>")[1].split("</option>")[0]+";"+container2[0].split("selected>")[0].split("value=\"")[1].split("\"")[0];
            semester= currentSem.split(";")[0].substring(currentSem.split(";")[0].length()-1).equals("1")?Course.FIRST_SEMESTER:Course.SECOND_SEMESTER;



            for(int i=0 ; i<classNum; i++){
                Log.d("tag", classContainer.length+"");
                SchoolClass sClass = new SchoolClass();
                Log.d("tag", classContainer[i]);
                sClass.setClassTime(Html.fromHtml(classContainer[i].split("time\">")[1].split("</td>")[0].split("&nbsp;")[0] + " " + classContainer[i].split("time\">")[1].split("</td>")[0].split("&nbsp;")[1]).toString());
                sClass.setClassPeriod(Integer.parseInt(classContainer[i].split("period\">P")[1].split("</td>")[0]));
                sClass.setClassTitle(Html.fromHtml(classContainer[i].split("class\">")[1].split("</td>")[0].replaceAll(" {2,}", "")).toString());
                teacherName = new Name(Html.fromHtml(classContainer[i].split("teacher\">")[1].split("</td>")[0].replaceAll(" {2,}","").split(",")[1].split("<br>")[0].trim()).toString(),Html.fromHtml(classContainer[i].split("teacher\">")[1].split("</td>")[0].replaceAll(" {2,}","").split(",")[0]).toString(),"Teacher");
                sClass.setTeacherName(teacherName);
                sClass.setRoomNumber(Html.fromHtml(classContainer[i].split("location\">")[1].split("</td>")[0]).toString());
                schoolClass.get(i).setFirstSemester(sClass);
                schoolClass.get(i).setSecondSemester(sClass);
            }

            for(int i=0;i<classNum;i++){
                String key = semIds.get(currentSem.split(";")[0]);
                settingClassObject(i, semester, key);
            }
            //settingPastClassObject(1-semester,semIds.get(container2[0].split("period\">")[1].split("</option>")[1-semester].split(">")[1]));
            getAttendance();
            for(Course course: schoolClass){
                SchoolClass sClass = semester==Course.FIRST_SEMESTER?course.getFirstSemester():course.getSecondSemester();

                Integer[] code = attendance.getAttendanceClass(sClass);
                String stringie = "";
                for(int i:code){
                    stringie+=i+"\t";
                }
                Log.d("tag", sClass.getClassTitle()+" :\t"+stringie);
            }

            getCalendar();
        }

        private void getCalendar() throws IOException {
            urlConnection = new URL(url+"calendar").openConnection();
            commonConnection(requestProperties);

            String container = response.toString().split("sec-box\"")[1].split("</div>")[0];
            String container2 = response.toString().split("sec-box")[2].split("<script")[0].split("event-key")[1].split("</ul>")[0];
            HashMap<String, String> key= new HashMap<>();
            for(int i=0; i<container2.split("</span>").length-1;i++){
                key.put(Html.fromHtml(container2.split("</span>")[i].split("<span class=\"")[1].split("\">")[0]).toString(), Html.fromHtml(container2.split("</span>")[i].split("<span ")[1].split("\">")[1]).toString());
            }

            String[] arr = container.split("<h4>");
            for (int i=1; i<arr.length; i++) {
                int month;
                int date;
                String monthStr = arr[i].split("</h4>")[0];

                switch (monthStr) {
                    case "January":
                        month = Calendar.JANUARY;
                        break;
                    case "February":
                        month = Calendar.FEBRUARY;
                        break;
                    case "March":
                        month = Calendar.MARCH;
                        break;
                    case "April":
                        month = Calendar.APRIL;
                        break;
                    case "May":
                        month = Calendar.MAY;
                        break;
                    case "June":
                        month = Calendar.JUNE;
                        break;
                    case "July":
                        month = Calendar.JULY;
                        break;
                    case "August":
                        month = Calendar.AUGUST;
                        break;
                    case "September":
                        month = Calendar.SEPTEMBER;
                        break;
                    case "October":
                        month = Calendar.OCTOBER;
                        break;
                    case "November":
                        month = Calendar.NOVEMBER;
                        break;
                    case "December":
                        month = Calendar.DECEMBER;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid month: " + monthStr);
                }

                String[] eventList = arr[i].split("</dd>");
                for (int j = 0; j < eventList.length - 1; j++) {
                    String day = eventList[j].split("</dt>")[0].split("<dt>")[1];
                    GregorianCalendar startDate = new GregorianCalendar();
                    GregorianCalendar endDate = new GregorianCalendar();
                    date = Integer.parseInt(day.substring(0, 2));

                    startDate.set(Calendar.MONTH, month);
                    endDate.set(Calendar.MONTH, month);
                    startDate.set(Calendar.DATE, date);
                    endDate.set(Calendar.DATE, date);
                    if (startDate.get(Calendar.MONTH) > Calendar.JULY) {
                        startDate.add(Calendar.YEAR, -1);
                        endDate.add(Calendar.YEAR, -1);
                    }
                    if (day.length() > 2) {
                        endDate.add(Calendar.DATE, Integer.parseInt(day.substring(3, 5)) - date);
                    }

                    String eventName = Html.fromHtml(eventList[j].split("<span")[1].split("</span>")[0].split("\">")[1]).toString();
                    String eventType = key.get(Html.fromHtml(eventList[j].split("<span")[1].split("class=\"")[1].split("\">")[0].split("&#")[0]).toString());
                    schoolCalendar.addEvent(new SchoolEvent(eventName, eventType, startDate, endDate));
                }

            }

        }

        // use later
        /**
         private void settingPastClassObject(int j, String semId) throws IOException {
         Grade grade;
         Category category;
         SchoolClass sc = new SchoolClass();
         String grade_container;

         urlConnection = new URL(url+"home/grades?period="+semId).openConnection();
         commonConnection(requestProperties);
         //TODO - look up and make it to actual split function
         String pastClassIds[] = response.toString().split("d");


         for (String pastClassId : pastClassIds) {
         urlConnection = new URL(url + "home/grades?period=" + semId + pastClassId).openConnection();
         commonConnection(requestProperties);
         grade_container = response.toString();
         if (!grade_container.contains("No grades for this class have been published.")) {
         for (int k = 0; k < grade_container.split("category-total\">").length; k++) {

         grade = new Grade(Long.parseLong(response.toString().split("category-total\">")[k + 1].split("score\">")[1].split("%</")[0]), null, null);
         category = new Category(response.toString().split("category-total\">")[k + 1].split("name\">")[1].split("</")[0], grade, Long.parseLong(response.toString().split("category-total\">")[k + 1].split("dimmed\">")[1].split("% of")[0]));
         clist.add(k, category);
         }
         sc.setAssignmentList(alist);
         sc.setCategory(clist);
         alist.clear();
         clist.clear();
         }
         }

         }
         */

        private void settingClassObject(int i, int j, String semId) throws IOException {
            Assignment assignment;
            Grade grade;
            Category category;
            SchoolClass sc = j==0?schoolClass.get(i).fsem:schoolClass.get(i).ssem;

            urlConnection = new URL(url+"home/assignments?classId="+classIds.get(sc.getClassTitle())).openConnection();
            commonConnection(requestProperties);
            String assignment_container = response.toString();
            urlConnection = new URL(url+"home/grades?period="+semId+"&classId="+classIds.get(sc.getClassTitle())).openConnection();
            commonConnection(requestProperties);
            String grade_container = response.toString();



            if(!assignment_container.contains("No assignments found")){
                String[] asgn_list = assignment_container.split("tbody")[1].split("</tr>");
                int gradeNum = 1;
                alist = new ArrayList<>();
                for(int k=0; k<asgn_list.length/2;k++){
                    assignment = new Assignment();
                    assignment.setDueDate(stringToDate(asgn_list[2 * k].split("</td>")[0].split("snug\">")[1].replaceAll("\\s", "").split("<")[0]));
                    ArrayList<String> files = new ArrayList<>();
                    if(asgn_list[2*k + 1].contains("Attachments")){
                        String[] fileArray = asgn_list[2*k + 1].split("_blank\">");
                        for(int l=0; l<fileArray.length-1;l++) {
                            files.add(Html.fromHtml(fileArray[l + 1].split("</a>")[0] + ";" + fileArray[l].split("href=\"")[1].split("\"")[0]).toString());
                        }
                    }
                    assignment.setFile(files);
                    assignment.setStatusMsg(Html.fromHtml(asgn_list[2 * k].split("</td>")[3].split("\">")[1].replace(" {2,}", "")).toString());

                    assignment.setTaskCategory(Html.fromHtml(asgn_list[2 * k].split("</td>")[2].split("<td")[1].substring(1)).toString());
                    assignment.setTaskTitle(Html.fromHtml(asgn_list[2 * k].split("</td>")[1].split("<td>")[1]).toString());
                    assignment.setTaskDetail(Html.fromHtml(asgn_list[2 * k + 1].split("<p>")[1].split("</p>")[0].replace(" {2,}", " ")).toString());
                    assignment.setTaskGrade(new Grade((double)100, "test", new GregorianCalendar()));
                    assignment.setWeight(-1);
                    if(Html.fromHtml(grade_container).toString().contains(assignment.getTaskTitle())) {
                        if(grade_container.split("</tr>")[gradeNum].contains(GregorianToString(assignment.getDueDate()))){
                            double lo = Double.parseDouble(grade_container.split("</tr>")[gradeNum].split("percent\">\\(")[1].split("%\\)</small>")[0]);
                            String str = grade_container.split("</tr>")[gradeNum].split("<td>")[3].split("</td>")[0];
                            GregorianCalendar gc = stringToDate(grade_container.split("</tr>")[gradeNum].split("dimmed\">")[3].trim());
                            grade = new Grade(lo, Html.fromHtml(str).toString(), gc);
                            assignment.setTaskGrade(grade);
                            String weight = grade_container.split("</tr>")[gradeNum].split("dimmed\">")[2].split("%</td>")[0];
                            assignment.setWeight(Double.parseDouble(weight));
                            gradeNum++;
                        }
                    }
                    alist.add(assignment);
                }
            }

            if(!grade_container.contains("No grades for this class have been published.")){
                grade = new Grade();
                grade.setGrade(Double.parseDouble(grade_container.split("percent\">\\(")[1].split("%\\)<")[0]));
                grade.setUpdatedDate(stringToDate(grade_container.split("As of ")[1].split("</small>")[0]));
                sc.setOverallGrade(grade);
                clist = new ArrayList<>();
                for(int k=0; k<grade_container.split("category-total\">").length-1;k++){
                    double art = Double.parseDouble(response.toString().split("category-total\">")[k + 1].split("score\">")[1].split("%</")[0].trim());
                    grade = new Grade(art,null, null);
                    category = new Category(response.toString().split("category-total\">")[k+1].split("name\">")[1].split("</")[0],grade,Double.parseDouble(response.toString().split("category-total\">")[k + 1].split("dimmed\">")[1].split("% of")[0]));
                    clist.add(k, category);
                }
                sc.setAssignmentList(alist);
                sc.setCategory(clist);
            } else
                sc.setOverallGrade(new Grade(0,null,null));
        }

        @SuppressLint("DefaultLocale")
        private void getAttendance() throws IOException{
            urlConnection = new URL(url+"home/attendance?view=year").openConnection();
            commonConnection(requestProperties);
            String container = response.toString().replaceAll(" {2,}","").replaceAll("> ",">").replaceAll(" <","<");
            Calendar c=Calendar.getInstance();
            c.set(Calendar.YEAR,Integer.parseInt(container.split("data-week=\"")[1].split("-W")[0]));
            c.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(container.split("data-week=\"")[1].split("-W")[1].split("\">")[0]));
            int week = c.get(Calendar.WEEK_OF_YEAR);
            for(int i=week;i<=Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);i++) {
                urlConnection = new URL(url + "home/attendance?view=week&week=" + c.get(Calendar.YEAR) + "-W" + String.format("%02d",i)).openConnection();
                commonConnection(requestProperties);
                if (response.toString().contains("No attendance records found")) {
                    continue;
                }
                container = response.toString().split("tbody")[1];
                String[] containers = container.replaceAll(" {2,}","").replaceAll("> ",">").replaceAll(" <","<").split("</td>");
                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                for(int j=0;j<5;j++){
                    for(int k=0;k<containers[j].split("<div class=\"class-period").length-1;k++){
                        HashMap<String, Integer> map = new HashMap<>();
                        String classTitle = containers[j].split("<div class=\"class-period")[k+1].split("<h5>")[2].split("</h5>")[0];
                        SchoolClass sc = null;
                        int attendanceCode = -1;
                        for(int l=0;l<classNum;l++){
                            if(schoolClass.get(l).getFirstSemester().getClassTitle().contains(classTitle)) {
                                if (semester == 0) {
                                    sc = schoolClass.get(l).getFirstSemester();
                                } else {
                                    sc = schoolClass.get(l).getSecondSemester();
                                }
                            }
                            switch (containers[j].split("<p>")[1].split("</p>")[0]){
                                case "Present":
                                    attendanceCode=Attendance.PRESENT;
                                    break;
                                case "Left Class (Excused)":
                                case "Late (Excused)":
                                    attendanceCode=Attendance.PARTIAL_EXCUSED;
                                    break;
                                case "Late (Unexcused)":
                                case "Left Class (Unexcused)":
                                    attendanceCode=Attendance.PARTIAL;
                                    break;
                                case "Absent (Excused)":
                                    attendanceCode=Attendance.ABSENT_EXCUSED;
                                    break;
                                case "Absent (Unexcused)":
                                    attendanceCode=Attendance.ABSENT;
                                    break;
                                case "Not Taken":
                                    attendanceCode=Attendance.NOT_TAKEN;
                                    break;
                                default:
                                    break;
                            }
                            assert sc != null;
                            map.put(sc.getClassTitle(), attendanceCode);
                            attendance.add(c, map);
                        }
                    }
                    c.add(Calendar.DATE,1);
                }
                c.set(Calendar.WEEK_OF_YEAR,i);
            }
        }

        private GregorianCalendar stringToDate(String string){
            int i= Integer.parseInt(string.split("/")[2])+2000;
            int j= Integer.parseInt(string.split("/")[0]);
            int k= Integer.parseInt(string.split("/")[1]);
            return new GregorianCalendar(i,j-1,k);
        }

        private String GregorianToString(GregorianCalendar gcal){
            return (gcal.get(GregorianCalendar.MONTH)+1)+"/"+gcal.get(GregorianCalendar.DATE)+"/"+(gcal.get(GregorianCalendar.YEAR)%2000);
        }

        private void commonConnection(String[][] propList) throws IOException {
            response = new StringBuffer();
            https = (HttpsURLConnection) urlConnection;
            https.setDoInput(true);
            https.setRequestMethod("GET");
            setRequestProperties(propList);
            https.connect();
            inputStream = https.getInputStream();
            BufferedReader br;
            if(https.getResponseCode()==200){
                GZIPInputStream gis = new GZIPInputStream(new BufferedInputStream(inputStream));
                br = new BufferedReader(new InputStreamReader(gis));
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
            }

        }

        private void setRequestProperties(String[][] properties){
            for (String[] property : properties) {
                https.setRequestProperty(property[0], property[1]);
            }
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(SCHOOL_CLASS,schoolClass);
                intent.putExtra(SEMESTER,semester);
                intent.putExtra(COOKIE,cookie);
                intent.putExtra(CALENDAR,schoolCalendar);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                System.out.println(System.currentTimeMillis()-startTime);
                startActivity(intent);
            } else if(status==405){
                Toast.makeText(LoginActivity.this, "Alma is currently undergoing maintenance", Toast.LENGTH_LONG).show();
            } else if(!connected){
                Toast.makeText(LoginActivity.this, "Error: Check your network status", Toast.LENGTH_LONG).show();
            } else if(errorCount!=0){
                mPasswordView.setError(getString(R.string.error_incorrect_password)+" You have "+Integer.toString(errorCount)+" attempts remaining.");
                mPasswordView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mPasswordView, 1);
            } else{
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mPasswordView, 1);
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}