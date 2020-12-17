import React from 'react';
import {Button, Image, InputNumber, Spin, Statistic} from 'antd';
import AppLayout from '../../components/AppLayout';
import {useTranslation} from 'react-i18next';
import i18n from "../../utils/i18n"
import {useHistory} from "react-router-dom";
import {useSelector} from "react-redux";
import moment from "moment";
import 'moment/locale/pl'
import 'antd/dist/antd.css';
import './AuctionDetailsPage.css'

const {Countdown} = Statistic;

const getDate = (date) => {
    const endDate = moment(date);
    const today = moment();
    const difference = endDate.diff(today, 'hours');

    if (i18n.language === 'pl')
        moment.locale('pl');

    if (difference <= 24 && difference > 0) {
        return `Today ${moment(endDate).format('h:mm')}`
    } else if (difference <= 48 && difference > 24)
        return `Tomorrow ${moment(endDate).format('h:mm')}`
    else
        return moment(endDate).format('dddd, DD MMMM YYYY, HH:mm')
}

const AuctionDetailsPage = (props) => {
    const {t} = useTranslation();
    const history = useHistory();
    const auctionDetails = props.auctionDetails;
    const currentRole = useSelector(state => state.user.currentRole);
    const username = useSelector(state => state.user.username);
    const currentPrice = auctionDetails ? (
        auctionDetails.bids.length > 0 ? (
            Math.max.apply(Math, auctionDetails.bids.map(function (o) {
                return o.price;
            }))
        ) : auctionDetails.startingPrice
    ) : null;

    const handleEditClick = () => {
        const currentLocation = history.location.pathname;

        history.push(`${currentLocation}/edit`);
    }

    const handleBidClick = () => {
        //    logic here
    }

    const extra = () => {
        if (currentRole === "MANAGER") {
            return (
                <>
                    <hr className="divider"/>
                    <div className="extra">
                        <Button type="primary" block
                                disabled={auctionDetails.userUsername !== username || moment().format() < auctionDetails.startDate}>
                            Edit
                        </Button>
                    </div>
                </>
            )
        } else if (currentRole === "CLIENT") {
            return (
                <>
                    <hr className="divider"/>
                    <div className="extra">
                        <div>
                            <InputNumber step={currentPrice * 0.01}
                                         min={currentPrice + 0.01}
                                         formatter={value => `PLN ${value}`}
                                         parser={value => value.replace(/PLN\s?|(,*)/g, '')}
                                         defaultValue={currentPrice}/>
                        </div>
                        <div style={{marginLeft: '10px'}}>
                            <Button type="primary" block
                                    disabled={auctionDetails.userUsername === username || moment().format() > auctionDetails.endDate}>
                                Place a bid
                            </Button>
                        </div>
                    </div>
                </>
            )
        } else
            return null
    }

    return (
        <AppLayout>
            {auctionDetails ? (
                <div className="auction-details-page-wrapper">
                    <div className="auction-card">
                        <div className="image">
                            <Image style={{cursor: "pointer"}} width={400}
                                   src={"data:image/png;base64," + auctionDetails.itemImage}
                                   alt={auctionDetails.itemImage}/>
                        </div>
                        <div className="details">
                            <div className="main">
                                <h1 style={{fontWeight: "bold"}}>{auctionDetails.itemName}</h1>
                                <span>Seller: {auctionDetails.userUsername}</span>
                            </div>
                            <hr className="divider"/>
                            <div className="countdown">
                                <Countdown title="Time left:" value={auctionDetails.endDate}/>
                                <span>&nbsp;|&nbsp;</span>
                                <span>{getDate(auctionDetails.endDate)}</span>
                            </div>
                            <hr className="divider"/>
                            <div className="bids">
                                <div className="current-bid">
                                    <span style={{color: "#767676"}}>Current bid</span>
                                    <span style={{fontSize: '24px'}}>
                                        <span>
                                            <strong>
                                                {currentPrice}
                                            </strong>
                                        </span>
                                        <span>
                                            <strong> PLN</strong>
                                        </span>
                                    </span>
                                </div>
                                <div className="bid-number">
                                    <span>
                                        <p style={{color: "#767676", marginBottom: 0}}>
                                            {auctionDetails.bids.length} bids
                                        </p>
                                    </span>
                                </div>
                            </div>
                            {extra()}
                        </div>
                    </div>
                </div>
            ) : (
                <Spin size="large"/>
            )}
        </AppLayout>
    );
}

export default AuctionDetailsPage;