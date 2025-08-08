import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import { ContextProvider } from "./contextApi/ContextApi.jsx";
import { QueryClient, QueryClientProvider } from "react-query";

// Whenever we use useQuery hool we have to wrap a query CLient out of the Application. 
const queryClient = new QueryClient();

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
    <ContextProvider>
      <App />
    </ContextProvider>
    </QueryClientProvider>
  </StrictMode>
);
