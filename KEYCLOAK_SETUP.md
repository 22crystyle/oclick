# Keycloak Configuration for OClick

This document provides instructions on how to configure Keycloak to act as an identity broker between OClick and hh.ru.

## Prerequisites

* A running Keycloak instance. The project includes a `docker-support.yml` file that can be used to start Keycloak.
* Admin access to the Keycloak admin console.

## 1. Realm Configuration

The application is configured to use the `oclick` realm. If it doesn't exist, you need to create it.

## 2. Client Configuration

You need to create a client in the `oclick` realm that the `shell` application will use to authenticate.

1. Go to **Clients** and click **Create client**.
2. **Client ID**: `oclick-shell`
3. **Name**: `OClick Shell`
4. Click **Next**.
5. **Client authentication**: `Off` (since this is a public client).
6. **Authorization**: `off`
7. **Authentication flow**:
    * Enable **OAuth 2.0 Device Authorization Grant**.
    * **OAuth 2.0 Device Code Lifespan**: Set as needed (e.g., 10 minutes).
8. Click **Save**.

## 3. Identity Provider for hh.ru

Now, configure Keycloak to use hh.ru as an external identity provider.

1. Go to **Identity Providers**.
2. Click **Add provider...** and select **OAuth v2**.
3. **Alias**: `hh-oauth2`
4. **Display name**: `HeadHunter`
5. Scroll down to **OAuth2 settings**.
6. **Authorization URL**: `https://hh.ru/oauth/authorize`
7. **Token URL**: `https://hh.ru/oauth/token`
8. **User Info URL**: `https://api.hh.ru/me`
9. **Client authentication**: `Client secret sent in the request body`.
10. **Client ID**: Your application's client ID from https://dev.hh.ru/admin.
11. **Client secret**: Your application's client secret from https://dev.hh.ru/admin.
12. Scroll down to **User profile claims**.
13. **ID Claim**: `id`
14. **Username Claim**: `email`
15. **Email Claim**: `email`
16. **Name Claim** let empty
17. **Given name Claim**: `first_name`
18. **Family name Claim**: `last_name`
19. Click **Save**.

## How it Works

1. The user runs the `login` command in the `oclick-shell`.
2. The shell contacts Keycloak to start the device flow and shows a user code and verification URL.
3. The user opens the URL and enters the code.
4. Keycloak prompts the user to log in. The user will see an option to log in with `HeadHunter`.
5. The user selects `HeadHunter` and is redirected to `hh.ru` to authorize the application.
6. Upon successful authorization, hh.ru redirects back to Keycloak.
7. Keycloak creates a token for the user and sends it to the polling `oclick-shell`.
8. The `oclick-shell` can now use this token to make authenticated requests to the OClick backend services through the
   API gateway.