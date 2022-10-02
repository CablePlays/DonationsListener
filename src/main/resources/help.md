# Authentication

In order for the plugin to be able to detect donations, it needs to first authenticated to whatever platform it is checking.

## Streamlabs

### Step 1: Generate An Authorization Code

To authenticate with streamlabs, you need to acquire an authorization code that can be found [here](https://www.streamlabs.com/api/v1.0/authorize?client_id=FFbbgsBieJjBxRUAb5rW2qS46AkEFTIofpAmisny&redirect_uri=http://localhost:8080/auth&response_type=code&scope=donations.read).
Click on approve, and you'll be taken to a page that says `This site can’t be reached`; that's expected.
Look at the page URL, and copy all text after `?code=` in the URL; this is your authorization code.

### Step 2: Request An Access Token

Next, use the authorization code to retrieve an access token which the plugin will use to query donations.
Open up a Windows terminal or any other console that supports and has cURL installed.
cURL comes preinstalled with the later versions of Windows (version 1803+).
To check if you have cURL installed, type `curl` into your console, and you should get the following output:
`curl: try 'curl --help' for more information`.
If you don't have cURL installed, you can do so [here](https://curl.se/download.html).

Once you have cURL, copy the following into your console:
```
curl --request POST "https://streamlabs.com/api/v1.0/token" -d "grant_type=authorization_code&client_id=FFbbgsBieJjBxRUAb5rW2qS46AkEFTIofpAmisny&client_secret=H4s12SHVw0hNw8jOAtcYg03enzESin8RHWa1I3jO&redirect_uri=http://localhost:8080/auth&code=<AUTHORIZATION CODE>"
```
Make sure that you replace `<AUTHORIZATION CODE>` at the end with the authorization code you acquired earlier.
Hit enter, and you should this output:
`{"access_token":"<ACCESS TOKEN>","token_type":"Bearer","expires_in":3600,"refresh_token":"<REFRESH TOKEN>"}`

### Step 3: Copy The Access Token

Copy whatever was given to you in the placeholder `ACCESS TOKEN` above, and paste it into the `config.yml` file.
The refresh token is irrelevant.

# Actions

An action is something that can happen when a certain donation happens.
For example, a skeleton spawning if someone donates over 50 dollars. Currently existing actions to test:

* Creepers: When `creeper` is included in the message,
will spawn n creepers where n is the amount of money donated rounded down (currency is not checked).
* Hello World: Will send a message `Hello World` to all players when someone donates something with `hello world`.
* Test: Will run on all donations and give you information about the donation.
