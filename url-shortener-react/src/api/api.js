import axios from 'axios'

// We will create a custom axios instance and use it in our component page to make api call.
export default axios.create({
    baseURL:import.meta.env.VITE_BACKEND_URL,
    
});
