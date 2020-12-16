import React from 'react';
import {Image, Spin} from 'antd';
import AppLayout from '../../components/AppLayout';
import {useTranslation} from 'react-i18next';
import {useHistory} from "react-router-dom";
import 'antd/dist/antd.css';
import './AuctionDetailsPage.css'

const AuctionDetailsPage = (props) => {
    const {t} = useTranslation();
    const history = useHistory();
    const auctionDetails = props.auctionDetails;

    const handleEditClick = () => {
        const currentLocation = history.location.pathname;

        history.push(`${currentLocation}/edit`);
    }

    const handleBidClick = () => {
        //    logic here
    }

    return (
        <AppLayout>
            {auctionDetails ? (
                <div className="auction-details-page-wrapper">
                    <div className="auction-card">
                        <Image width={400} src={"data:image/png;base64," + auctionDetails.itemImage}
                               alt={auctionDetails.itemImage}/>

                    </div>
                    <div className="bids-history">

                    </div>
                </div>
            ) : (
                <Spin size="large"/>
            )}
        </AppLayout>
    );
}

export default AuctionDetailsPage;