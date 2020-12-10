import React from 'react';
import AuctionCard from '../../components/Card'
import 'antd/dist/antd.css'
import './AuctionListPage.css'

const AuctionListPage = (props) => {
    return (
        <div className="auction-list-wrapper">
            {props.auctions.map((auction) => (
                <AuctionCard auction={auction} key={auction.id}/>
            ))}
        </div>
    );
}

export default AuctionListPage;