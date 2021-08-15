# Timekeeper

The service fetches data from a Google sheet and provides processed data for Skype Bot webhook.

## Installation

### Requirements

* Java
* Maven
* MongoDB

Make sure that you are in the root directory of the app and then run

> mvn package
 
To run jar file

>java -jar timekeeper-{version}.jar

## Configuration 

The app configuration can be changed in `./src/main/resources/application.properties`

#### arguments

Values can be configured by providing arguments:

The following example will override the default scheduled data fetching 

>java -jar timekeeper-{version}.jar --meeting.update-schedule=0 0 9 * * 2

#### required environment arguments

SKYPE_APP_ID - could be taken on Azure portal

SKYPE_PASSWORD - could be taken on Azure portal

SPREADSHEET_ID - could be taken from url of your Google sheet document

The following variables need to be configured correctly:

#### property file

> java -jar timekeeper-{version}.jar --spring.config.location=file:{somewhere}/application-external.properties
 
## How to create and manage a Skype bot

[Create a bot](https://dev.botframework.com/bots)

[Manage a bot](https://portal.azure.com/)

**It's important to change the bot manifest file and create secrets**

Login on the Azure portal and go to the Manifest section of the bot. Change the following value:

> "signInAudience": "AzureADandPersonalMicrosoftAccount"
 
Go to the section Certificates and secrets and create a new one client secret. Use value of the secret as a bot password. 

## How to create a Google credential

Login on the [Google console](https://console.cloud.google.com/). Create a new project and enable the Google Sheets API.
Go to the section Credentials and create a new one OAuth 2.0 Client ID. Download the JSON file and put it to the `resources` 
folder with name `client_secret.json`. Make sure that the Timekeeper has been started. Find in the logs redirect URI and
set this value as Authorized redirect URI on the Google console. Then pass authentication through your browser.