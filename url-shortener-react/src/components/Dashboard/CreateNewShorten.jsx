import React, { useState } from "react";
import { useStoreContext } from "../../contextApi/ContextApi";
import { useForm } from "react-hook-form";
import TextField from "../TextField";
import { Tooltip } from '@mui/material';
import { RxCross2 } from 'react-icons/rx';
import api from "../../api/api"
import toast from "react-hot-toast";

const CreateNewShorten = ({ setOpen, refetch }) => {
  const { token } = useStoreContext();
  const [loading, setLoading] = useState(false);
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors }
  } = useForm({
    defaultValues: {
     originalUrl: "",
    },
    mode: "onTouched"
  });

  const createShortUrlHandler = async (data)=>{
    setLoading(true);
    try {
          const { data : res } = await api.post("/api/urls/shorten", data, {
             headers:{
              "Content-Type":"application/json",
              Accept: "application/json",
              Authorization: "Bearer "+token,
             }
          })
        // We will be sent a slug but not a proper url with domain. Now we have to prepare the final version of 
        // that. We add a subdomain for every created short url.
        const shortenUrl = `${import.meta.env.VITE_BACKEND_URL}/${res.shortUrl}`;
        // Now we copy the url in clipboard.
        navigator.clipboard.writeText(shortenUrl).then(()=>{
          toast.success("Short URL copied to clipboard");
        })
        reset();
        setOpen(false);
    } catch (error) {
       toast.error("Create short url failed");
       console.log(error);
    }finally{
      setLoading(false);
    }
  };

  return (
  <div className="flex justify-center items-center bg-white rounded-lg">
    <form
     onSubmit={handleSubmit(createShortUrlHandler)}
     className="sm:w-[450px] w-[360px] relative shadow-lg pt-8 pb-5 sm:px-8 px-4 rounded-lg"
    >
    <h1 className="sm:mt-0 mt-3 font-sans text-center font-bold text-[22px] text-slate-700 sm:text-2xl">
        Create New Shorten Url
    </h1>
    <hr className="mt-2 sm:mb-5 mb-3 border-1 border-slate-400" />
    <div>
        <TextField 
         label="Enter URL"
         required
         id="originalUrl"
         placeholder="https://www.example.com"
         type="url"
         message="Url is required"
         register={register}
         errors={errors}
        />
    </div>
    <button
     className="bg-blue-600 hover:opacity-85 font-semibold text-white w-32 py-2 cursor-pointer rounded-md my-3"
     type="text"
    >
      {loading ? "Loading..." : "Create"}
    </button>
    {!loading && 
      <Tooltip title="Close">
            <button
              disabled={loading}
              onClick={() => setOpen(false)}
              className=" absolute right-2 top-2  "
            >
              <RxCross2 className="text-slate-800   text-3xl" />
            </button>
          </Tooltip>
    }
    </form>
  </div>
)
};

export default CreateNewShorten;
