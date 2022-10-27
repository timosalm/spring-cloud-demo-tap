import { AuthConfig } from 'angular-oauth2-oidc';
import {environment} from "../../environments/environment";

export const authCodeFlowConfig: AuthConfig = {
  issuer: environment.authConfig.issuer,
  redirectUri: window.location.origin + environment.baseHref + 'index.html',
  clientId:  environment.authConfig.clientId,
  responseType: 'code',
  scope: 'openid offline_access email profile roles',
  showDebugInformation: true,
  timeoutFactor: 0.01,
  logoutUrl: environment.authConfig.issuer + '/logout',
  redirectUriAsPostLogoutRedirectUriFallback: true
};
