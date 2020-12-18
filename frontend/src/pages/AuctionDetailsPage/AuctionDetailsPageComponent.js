import React from 'react';
import {Button, Image, InputNumber, Spin, Statistic, Tabs, Timeline} from 'antd';
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
const {TabPane} = Tabs;

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

    const getDate = (date) => {
        const today = moment();
        const difference = date.diff(today, 'day');


        if (i18n.language === 'pl')
            moment.locale('pl');

        if (date.startOf('day').isSame(today.startOf('day')))
            return (
                <>
                    <Countdown value={date}/>
                    <span style={{marginRight: '0.5em', marginLeft: '0.5em'}}>|</span>
                    <span>Today {moment(date).format('h:mm')}</span>
                </>
            );
        else if (date.startOf('day').isSame(today.add(1, 'day').startOf('day')))
            return (
                <>
                    <Countdown value={date}/>
                    <span style={{marginRight: '0.5em', marginLeft: '0.5em'}}>|</span>
                    <span>Tomorrow {moment(date).format('h:mm')}</span>
                </>
            );
        else
            return (
                <>
                    <span>{difference} days</span>
                    <span style={{marginRight: '0.5em', marginLeft: '0.5em'}}>|</span>
                    <span>{moment(date).format('dddd, DD MMMM YYYY, HH:mm')}</span>
                </>
            );
    }

    const time = () => {
        const today = moment();
        const startDate = moment(auctionDetails.startDate);
        const endDate = moment(auctionDetails.endDate);

        if (startDate > today)
            return (
                <>
                    <span style={{marginRight: '0.5em'}}>To start:</span>
                    {getDate(startDate)}
                </>
            );
        else if (endDate >= today && startDate <= today)
            return (
                <>
                    <span style={{marginRight: '0.5em'}}>Time left:</span>
                    {getDate(endDate)}
                </>
            );
        else
            return (
                <>
                    <span style={{marginRight: '0.5em'}}>Ended:</span>
                    {moment(endDate).format('dddd, DD MMMM YYYY, HH:mm')}
                </>
            );
    }

    const extra = () => {
        if (currentRole === "MANAGER") {
            return (
                <>
                    <hr style={{marginBottom: 'auto'}} className="divider"/>
                    <div className="extra">
                        <Button type="primary" block
                                disabled={auctionDetails.userUsername !== username || moment().format() < auctionDetails.startDate}>
                            Edit
                        </Button>
                    </div>
                </>
            );
        } else if (currentRole === "CLIENT") {
            return (
                <>
                    <hr style={{marginBottom: 'auto'}} className="divider"/>
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
            );
        } else
            return null;
    }

    return (
        <AppLayout>
            {auctionDetails ? (
                <div className="auction-details-page-wrapper">
                    <div className="auction-card">
                        <div className="image-wrapper">
                            <Image className="image"
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
                                {time()}
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
                    <div className="additional-info">
                        <Tabs defaultActiveKey="1">
                            <TabPane tab="Description" key="1">
                                <div className="description">
                                    <span>{auctionDetails.itemDescription}</span>
                                </div>
                            </TabPane>
                            <TabPane tab="Bid history" key="2">
                                <div className="history">
                                    <Timeline>
                                        {auctionDetails.bids.reverse().map(bid => {
                                            return (
                                                <Timeline.Item>{moment(bid.date).format('dddd, DD MMMM YYYY, HH:mm')} - {bid.userUsername} placed {bid.price} PLN</Timeline.Item>
                                            );
                                        })}
                                    </Timeline>
                                </div>
                            </TabPane>
                        </Tabs>
                    </div>
                </div>
            ) : (
                <Spin size="large"/>
            )}
        </AppLayout>
    );
}

export default AuctionDetailsPage;