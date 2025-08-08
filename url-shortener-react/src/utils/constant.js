// This is for sub domain configuration. We render the App based on our url.

import AppRouter, { SubDomainRouter } from "../AppRouter";

export const subDomainList = [
    {subdomain:"", app: AppRouter, main: true},
    {subdomain:8, app: SubDomainRouter, main: false}
];

