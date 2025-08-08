import React, { useEffect } from 'react'
import { useParams } from 'react-router-dom'

const ShortenUrlPage = () => {
    const { url } = useParams();

    // Here we hit our backend API with backend base url and it will redirect our original url of that
    // particular short url. 
    
    useEffect(() => {
        if (url) {
            window.location.href = import.meta.env.VITE_BACKEND_URL + `/${url}`;
           
        }
    }, [url]);
  return <p>Redirecting...</p>;
}

export default ShortenUrlPage