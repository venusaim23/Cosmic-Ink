# Cosmic-Ink

Cosmic Ink is a transcript application which was built with the help of Symbl AI and At Sign platform for back-end to store our data and authenticate. It process vast sum of conversations and summarizes them based on the following categories: Topics discussed, Messages Sent, Questions asked, Actions performed, Follow-ups. It also contains Analyser that presents total silence time, total talk time, total overlaps, and so on, which helps track effectiveness of the conversation.
It saves time of the user rather going the conventional way and provides a window for their quality time.

## Main Features
### Analyse Audio/Video
Upload the URL of the required audio/video in the provided space and press the button, GO! Now, Symbl AI will provide u all the insights of the conversations.

### Join a meeting
As for joining a meeting, provide a valid Phone number, DTMF code and email address that receives the transcripts and press the button, GO! Now, Symbl AI wil provide the live transcripts and other insights of the meeting.

## How to fork?
### Step 1: Fork the repository
Forking the repository will create a copy of the repository in your gitHub where you can add custom features and build a different version of the app for yourself. You can also, contribute to the template by suggesting necessary changes through issues.

### Step 2: Clone your repository
After forking the repository head over to your fork and clone the repository in your local system. There are two ways to clone your repository. Copy the URL of your fork and:

Open Android Studio and click on `Get from VCS`. Type the URL that you copied and select the destination. Your project will be cloned and open in Android Studio.

(OR)

Open Terminal, navigate to the folder where you want to store the project and type `git clone "URL that you copied"`. A folder will be created with the app name. Open the project with Android Studio.
Your JDK version might mismatch, so select "Use Embedded JDK" when prompted in android studio.

### Step 3: Sign up on Symbl AI platform and grab your security keys
After the build is successful, sign up to Symbl AI to get the security keys. Create a fle `Credentials.java`. Copy the following code template.

```java
package com.hack.cosmicink.Utilities;

public class Credentials {
    public static final String appID = "app_id";
    public static final String appSecret = "app_secret";
    public static String accessToken;
}
```

Paste the corresponding keys in this file and place it at the location specified -> `app/src/main/java/com/hack/cosmicink/Utilities`

### Step 4: Run the app
Once your application is built successfully and runs on your emulator/mobile, you can add more functionality in the app and make your own version of it.

## Collaboration
If you are interested in our project, you can fork the repository and try the application in your local system. The instructions are mentioned above. You are welcomed to make contributions to our project and we are more than happy to help you set up things and provide relevant resources. Start by creating issues and make a PR once the issue is resolved.
