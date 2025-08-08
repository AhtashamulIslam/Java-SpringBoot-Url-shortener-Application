import { createContext, useState,useContext } from "react";

// We create a context after a user is authenticated, we get the token from local storage.
const ContextApi = createContext()

export const ContextProvider = ({children})=>{

     const getToken = localStorage.getItem("JWT_TOKEN")
                     ? JSON.parse(localStorage.getItem("JWT_TOKEN")) : null;
     const [token,setToken] = useState(getToken);

     const sendData = { // These are accessible from throughout the application. 
        token,
        setToken
     }

     return <ContextApi.Provider value={sendData}>{children}</ContextApi.Provider>

};


export const useStoreContext = ()=>{
     const context = useContext(ContextApi);
     return context;
}