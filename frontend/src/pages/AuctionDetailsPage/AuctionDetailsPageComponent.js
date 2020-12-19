import React, {useState} from 'react';
import {Button, Image, InputNumber, Spin, Statistic, Tabs, Timeline, Tooltip} from 'antd';
import AppLayout from '../../components/AppLayout';
import {useTranslation} from 'react-i18next';
import i18n from "../../utils/i18n"
import {useHistory} from "react-router-dom";
import {useSelector} from "react-redux";
import moment from "moment";
import 'moment/locale/pl'
import 'moment/locale/en-gb'
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
    const [timeItem, setTimeItem] = useState(null);
    const [extraItem, setExtraItem] = useState(null);

    const handleEditClick = () => {
        const currentLocation = history.location.pathname;

        history.push(`${currentLocation}/edit`);
    }

    const handleBidClick = () => {
        //    logic here
    }

    const getDate = (date) => {
        const today = moment();
        const dateDay = moment(date).startOf('day');

        if (i18n.language === 'pl')
            moment.locale('pl');
        else
            moment.locale('en-gb');

        if (dateDay.isSame(today.startOf('day')))
            return (
                <>
                    <Countdown value={date} onFinish={() => update()}/>
                    <span style={{marginRight: '0.5em', marginLeft: '0.5em'}}>|</span>
                    <span>{t('text.today')} {date.format('H:mm')}</span>
                </>
            );
        else if (dateDay.isSame(today.add(1, 'day').startOf('day')))
            return (
                <>
                    <Countdown value={date} onFinish={() => update()}/>
                    <span style={{marginRight: '0.5em', marginLeft: '0.5em'}}>|</span>
                    <span>{t('text.tomorrow')} {date.format('H:mm')}</span>
                </>
            );
        else
            return (
                <>
                    <Countdown value={date} format="D" onFinish={() => update()}/>
                    <span style={{fontWeight: 'bold', marginLeft: '0.5em'}}>{t('text.days')}</span>
                    <span style={{marginRight: '0.5em', marginLeft: '0.5em'}}>|</span>
                    <span>{date.format('dddd, DD MMMM YYYY, H:mm')}</span>
                </>
            );
    }

    const timer = () => {
        const today = moment();
        const startDate = moment(auctionDetails.startDate);
        const endDate = moment(auctionDetails.endDate);

        if (startDate > today)
            return (
                <>
                    <span>{t('text.toStart')}:</span>
                    {getDate(startDate)}
                </>
            );
        else if (endDate >= today && startDate
            <= today)
            return (
                <>
                    <span>{t('text.timeLeft')}:</span>
                    {getDate(endDate)}
                </>
            );
        else
            return (
                <>
                    <span style={{marginRight: '0.5em'}}>{t('text.ended')}:</span>
                    {endDate.format('dddd, DD MMMM YYYY, HH:mm')}
                </>
            );
    }

    const extra = () => {
        if (currentRole === "MANAGER") {
            const isDisabled = auctionDetails.userUsername !== username || moment() > moment(auctionDetails.startDate);

            return (
                <>
                    <hr style={{marginBottom: 'auto'}} className="divider"/>
                    <div className="extra">
                        {isDisabled ? (
                            <Tooltip title={t('text.editForbidden')} color={"red"}>
                                <Button type="primary" block disabled={isDisabled} onClick={handleEditClick}>
                                    {t('text.edit')}
                                </Button>
                            </Tooltip>
                        ) : (
                            <Button type="primary" block onClick={handleEditClick}>
                                {t('text.edit')}
                            </Button>
                        )}
                    </div>
                </>
            );
        } else if (currentRole === "CLIENT") {
            const isDisabled = auctionDetails.userUsername === username ||
                moment() > moment(auctionDetails.endDate) ||
                moment() < moment(auctionDetails.startDate);

            return (
                <>
                    <hr style={{marginBottom: 'auto'}} className="divider"/>
                    <div className="extra">
                        <div>
                            <InputNumber step={currentPrice * 0.01}
                                         min={currentPrice + 0.01}
                                         defaultValue={currentPrice}
                                         formatter={value => `${value} PLN`}
                                         parser={value => value.replace(/PL|PN|LN|P|N|L|\s?|(,*)/g, '')}/>
                        </div>
                        <div style={{marginLeft: '10px'}}>
                            {isDisabled ? (
                                <Tooltip title={t('text.bidForbidden')} color={"red"}>
                                    <Button type="primary"
                                            disabled={isDisabled}>
                                        {t('text.placeBid')}
                                    </Button>
                                </Tooltip>
                            ) : (
                                <Button type="primary">
                                    {t('text.placeBid')}
                                </Button>
                            )}
                        </div>
                    </div>
                </>
            );
        } else
            return null;
    }

    const update = () => {
        setTimeItem(timer());
        setExtraItem(extra());
    }

    return (
        <AppLayout>
            {auctionDetails ? (
                <div className="auction-details-page-wrapper">
                    <div className="auction-card">
                        <div className="image-wrapper">
                            <Image src={"data:image/png;base64," + auctionDetails.itemImage}
                                   alt={auctionDetails.itemImage}/>
                        </div>
                        <div className="details">
                            <div className="main">
                                <h1 style={{fontWeight: "bold"}}>{auctionDetails.itemName}</h1>
                                <span>{t('text.seller')}: {auctionDetails.userUsername}</span>
                            </div>
                            <hr className="divider"/>
                            <div className="countdown">
                                {timeItem ? timeItem : timer()}
                            </div>
                            <hr className="divider"/>
                            <div className="bids">
                                <div className="current-bid">
                                    <span style={{color: "#767676"}}>{t('auctionLabels.currentPrice')}</span>
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
                                            {t('auctionLabels.bidsNumber')}: {auctionDetails.bids.length}
                                        </p>
                                    </span>
                                </div>
                            </div>
                            {extraItem ? extraItem : extra()}
                        </div>
                    </div>
                    <div className="additional-info">
                        <Tabs defaultActiveKey="1">
                            <TabPane tab={t('auctionLabels.itemDescription')} key="1">
                                <div className="description">
                                    <span>{auctionDetails.itemDescription}</span>
                                </div>
                            </TabPane>
                            <TabPane tab={t('text.bidHistory')} key="2">
                                <div className="history">
                                    <Timeline>
                                        {auctionDetails.bids.reverse().map(bid => {
                                            return (
                                                <Timeline.Item
                                                    key={bid.id}>{moment(bid.date).format('dddd, DD MMMM YYYY, HH:mm')} - {bid.userUsername} {t('text.bid')} {bid.price} PLN</Timeline.Item>
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