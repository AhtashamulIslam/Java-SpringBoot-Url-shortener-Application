import { useQuery } from "react-query";
import api from "../api/api";


// Fetch all the short urls of an authenticated user. 
export const useFetchMyShortUrls = (token, onError) => {
    return useQuery("my-shortenurls",
         async () => {
            return await api.get(
                "/api/urls/myurls",
            {
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json",
                    Authorization: "Bearer " + token,
                },
            }
        );
    },
          {
            select: (data) => {
                const sortedData = data.data.sort(
                    (a, b) => new Date(b.createdDate) - new Date(a.createdDate)
                );
                return sortedData;
            },
            onError,
            staleTime: 5000
          }
        );
};
// We fetch the number of clicks of an authenticated user.
export const useFetchTotalClicks = (token, onError) => {
  // token is JWT and onError is a call back function to handle the errors during the
  // query execution
  return useQuery(
    "url-totalclick",
    // 1st parameter is query key, 2nd is an async function to make an API call,
    // 3rd one is options object(tranformed data/response may come over here,error handling func, catching options
    //  can be hold here)
    async () => {
      return await api.get(
        "/api/urls/totalClicks?startDate=2025-01-01&endDate=2025-12-31",
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: "Bearer " + token
          }
        }
      );
    },
    // Here we transform response, handle error and caching as well.
    {
      select: (data) => {
        // Here we tranform raw data before passing it to the component.
        // We convert the data into an array of objects where key is the date.
        // And group them as an object of clickDate and count properties.
        // We will get the response as
        // data.data =>
        // {
        //     "2025-01-01":120,
        //      "2025-01-02":111,
        //       "2025-01-03":90,
        // }
        // Object.keys(data.data) => ["2025-01-01","2025-01-02","2025-01-03"]
        // So each time we iterate we will get the date as key.
        const convertToArray = Object.keys(data.data).map((key) => ({
          clickDate: key,
          count: data.data[key]
        }));
        // FINAL :
        //   [
        //     { clickDate :"2025-01-01",count: 120 },
        //      { clickDate :"2025-01-02",count: 111 },
        //       { clickDate :"2025-01-03",count: 90 }
        //   ]
        return convertToArray;
      },
      onError,
      staleTime: 5000 // Amount of cache time that data will be cached here.
    }
  );
};



