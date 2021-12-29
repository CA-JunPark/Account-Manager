package com.example.passwordmanager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class BCryptPassword{
    private static int salt = 7;

    /**
     * save hashedPw into json file (pw.json)
     * @param textPassword
     * @throws IOException
     */
    public static boolean hashPassword(String textPassword){
        try{
            // does not accept empty String
            if (textPassword == ""){
                Controller.AlertErrorWindow("Empty String Password Will Not Be Accepted");
                return false;
            }

            // hash pw
            String hashedPw = BCrypt.hashpw(textPassword, BCrypt.gensalt(salt));

            // save it as JSONObject
            JSONObject pwJSON = new JSONObject();
            pwJSON.put("password", hashedPw);

            FileWriter JSONWriter = new FileWriter("pw.json");
            JSONWriter.write(pwJSON.toJSONString());
            JSONWriter.flush();
            JSONWriter.close();
            return true;
        }
        catch(Exception e){
            Controller.AlertErrorWindow(String.valueOf(e));
            return false;
        }
    }

    /**
     * check enteredPw is currently hashed pw
     * @param enteredPw
     * @throws FileNotFoundException
     */
    public static boolean checkPw(String enteredPw) throws IOException, ParseException {
        // get current hashedPw
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("pw.json");
        Object obj = jsonParser.parse(reader);
        JSONObject pwJson = (JSONObject) obj;
        String hashedPw = (String) pwJson.get("password");

        return BCrypt.checkpw(enteredPw, hashedPw);
    }

    /**
     * change current password to newPw
     * @param newPw
     * @throws IOException
     * @throws ParseException
     */
    public static void changePw(String newPw) throws IOException, ParseException {
        // does not accept empty String
        if (newPw == ""){
            Controller.AlertErrorWindow("Empty String Password Will Not Be Accepted");
            return;
        }
        // edit current hashedPw
        FileReader reader = new FileReader("pw.json");
        JSONParser parser = new JSONParser();
        JSONObject pwJson = (JSONObject) parser.parse(reader);
        pwJson.put("Password", BCryptPassword.hashPassword(newPw));
    }
}
