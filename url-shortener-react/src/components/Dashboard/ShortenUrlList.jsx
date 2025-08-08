import React from 'react'
import ShortenUrlItem from './ShortenUrlItem';

const ShortenUrlList = ({data}) => {
  return (
    <div className='my-6 space-y-4'>
       {data.map((item)=>(
        <ShortenUrlItem key={item.id} {...item}/>
       ))}
    </div>
  )
}

export default ShortenUrlList;