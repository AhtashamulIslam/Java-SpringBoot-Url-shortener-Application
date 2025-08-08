import { subDomainList } from "./constant";

export const getApps = () => {
    const subdomain = getSubDomain(window.location.href);

    const mainApp = subDomainList.find((app) => app.main);
    if (subdomain === "") return mainApp.app;

    const apps = subDomainList.find((app) => subdomain === app.subdomain);

    return apps ? apps.app : mainApp.app;
}

// url.localhost  ( return null for our main App)
// url.urlbestshort.com ( return url which will reditrect to the original url)
export const getSubDomain = (location) => {
    const locationParts = location.split("/");
    const isLocalhost = locationParts[3].length;
    return isLocalhost;
   
};