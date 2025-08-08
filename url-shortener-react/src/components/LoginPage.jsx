import { useForm } from 'react-hook-form'
import TextField from './TextField';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api/api'
import toast from 'react-hot-toast';
import { useStoreContext } from '../contextApi/ContextApi';

const LoginPage = () => {

    const [loader,setLoader] = useState();
    const navigate = useNavigate();
    const {setToken} = useStoreContext(); // We set logged in user token in our context using custom hook.

      const {
              register,
              handleSubmit,
              reset,
              formState:{errors},

            }= useForm({
                defaultValues:{
                    username:"",
                    email:"",
                    password:""
                },
                mode:"onTouched",
            });

  // API for register. 
   const loginHandler = async (data)=>{
        setLoader(true);
        try {
           const {data : response } = await api.post(
                "/api/auth/public/login",
                data
            );
            setToken(response.token) // We will get the token and taki it in our context. 
          // As we will get a token after authorization, we will store this in local store of browser.
          localStorage.setItem("JWT_TOKEN",JSON.stringify(response.token));
            toast.success("Login Successful!")
            reset(); // Reset the form after successfully post the data. 
            navigate("/dashboard");
           
        } catch (error) {
            console.log(error);
            toast.error("Login Failed!")
        }finally{
            setLoader(false);
        }
   }

  return (
    <div 
       className='min-h-[calc(100vh-64px)] flex justify-center items-center'
    >
        <form onSubmit={handleSubmit(loginHandler)}
          className='sm:w-[450px] w-[360px] shadow-md py-8 sm:px-8 px-4 rounded-md'
        >
          <h1 className='text-center font-serif text-blue-500 font-bold lg:text-3xl text-2xl'>
            Login Here 
          </h1>
          <hr className='mt-2 mb-5 border-1 border-slate-400'/>
          <div className='flex flex-col gap-3'>
            <TextField 
               label='UserName'
               required
               id='username'
               type='text'
               message='username is required'
               placeholder='username'
               register={register}
               errors={errors}

               />
              
               <TextField 
               label='Password'
               required
               id='password'
               type='password'
               message='password is required'
               placeholder='password'
               register={register}
               errors={errors}
               />
          </div>
          <button 
           disabled={loader}
            className='rounded-lg p-3 bg-blue-500 w-full mt-4 font-semibold text-white hover:opacity-90 cursor-pointer'
          >
            { loader ? 'Loading...' :'Login'}
          </button>
           <div className="flex gap-2 mt-4 justify-center text-slate-600 text-sm">
          <p>Don't have an account?</p>
          <Link to="/register">
            <span className="ml-1/2 text-blue-700 hover:underline">SignUp</span>
          </Link>
        </div>
        </form>
    </div>
  )
}

export default LoginPage