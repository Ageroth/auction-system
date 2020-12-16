import React from 'react';
import {Link} from 'react-router-dom';
import './Card.css'

const AuctionCard = (props) => {
    const {auction} = props;
    return (
        <div className="auction-card">
            <div className="image-wrapper">
                <Link to={`/auctions/${auction.id}`}>
                    <img className="image" src={"data:image/png;base64," + auction.itemImage} alt="item image"/>
                </Link>
            </div>
            <div className="main">
                <h1>
                    <Link to={`/auctions/${auction.id}`}>
                        {auction.itemName}
                    </Link>
                </h1>
                <p>{auction.itemDescription}</p>
            </div>
            <div className="aside">
                <div className="lot-details-wrapper">
                    <ul className="lot-details">
                        <li className="current-price">
                            <span>Current bid</span>
                            <span>
                                <span>
                                    <strong>
                                        {auction.currentPrice ? auction.currentPrice : auction.startingPrice}
                                    </strong>
                                </span>
                                <span>
                                    <strong> PLN</strong>
                                </span>
                            </span>
                        </li>
                        <li className="bids-number">
                            <span>Bids number</span>
                            <span>
                                <strong>{auction.bidsNumber}</strong>
                            </span>
                        </li>
                    </ul>
                </div>
                <footer>
                    <div className="date">
                        <span>Date:</span>
                        <strong>{auction.startDate} </strong>
                        <strong>to {auction.endDate}</strong>
                    </div>
                </footer>
            </div>
        </div>
    );
};

export default AuctionCard;