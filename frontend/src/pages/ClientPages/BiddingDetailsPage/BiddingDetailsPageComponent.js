import React, {useState} from 'react';
import {Button, Form, Image, InputNumber, Popconfirm, Spin, Statistic, Tabs, Timeline, Tooltip} from 'antd';
import AppLayout from '../../../components/AppLayout';
import {useTranslation} from 'react-i18next';
import i18n from "../../../utils/i18n"
import {useSelector} from "react-redux";
import moment from "moment";
import 'moment/locale/pl'
import 'moment/locale/en-gb'
import 'antd/dist/antd.css';
import './BiddingDetailsPage.css'

const {Countdown} = Statistic;
const {TabPane} = Tabs;

const BiddingDetailsPage = (props) => {
    const [visible, setVisible] = useState(false);
    const [form] = Form.useForm();
    const {t} = useTranslation();
    moment.locale(i18n.language);
    const auctionDetails = props.auctionDetails;
    const username = useSelector(state => state.user.username);
    const [timeItem, setTimeItem] = useState(null);
    const [extraItem, setExtraItem] = useState(null);
    let maxBid = auctionDetails ? (
        auctionDetails.bids.length > 0 ? (
            auctionDetails.bids.reduce((max, bid) => max.price > bid.price ? max : bid)
        ) : null
    ) : null
    const currentPrice = auctionDetails ? (
        maxBid ? maxBid.price : auctionDetails.startingPrice
    ) : null

    const onFinish = (values) => {
        const payload = Object.assign({}, values);
        props.onSubmit(payload);
    }

    const showPopconfirm = () => {
        setVisible(true);
    }

    const handleCancel = () => {
        setVisible(false);
    }

    const handleOk = () => {
        setVisible(false);
        form.submit();
    }

    const getDate = (date) => {
        const today = moment();
        const dateDay = moment(date).startOf('day');

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
        const isDisabled = auctionDetails.userUsername === username ||
            moment() > moment(auctionDetails.endDate) ||
            moment() < moment(auctionDetails.startDate);

        return (
            <>
                <div className="extra">
                    {isDisabled ? (
                        <>
                            <InputNumber style={{marginRight: "15px"}} defaultValue={currentPrice + 0.01}
                                         precision={2} formatter={value => `${value} PLN`} readOnly={true}/>
                            <Tooltip title={t('text.bidForbidden')} color={"red"}>
                                <Button type="primary" disabled={true}>
                                    {t('text.placeBid')}
                                </Button>
                            </Tooltip>
                        </>
                    ) : (
                        <Form
                            form={form}
                            name="bid_place_form"
                            className="bid-place-form"
                            scrollToFirstError
                            onFinish={onFinish}
                        >
                            <Form.Item
                                name="price"
                                initialValue={currentPrice + 0.01}
                                rules={[
                                    {
                                        required: true,
                                        message: t('validation.required')
                                    },
                                ]}
                            >
                                <InputNumber step={0.01} precision={2} min={currentPrice + 0.01}
                                             formatter={value => `${value} PLN`}
                                             parser={value => value.replace(/PL|PN|LN|P|N|L|\s?|(,*)/g, '')}/>
                            </Form.Item>

                            <Form.Item>
                                <Popconfirm
                                    title={t('text.areYouSure')}
                                    visible={visible}
                                    onConfirm={handleOk}
                                    onCancel={handleCancel}
                                    okText={t('text.yes')}
                                    cancelText={t('text.no')}
                                >
                                    <Button type="primary" loading={props.isSubmitting}
                                            onClick={showPopconfirm}>{t('text.placeBid')}</Button>
                                </Popconfirm>
                            </Form.Item>
                        </Form>
                    )}
                </div>
            </>
        );
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
                            <hr className="divider"/>
                            <div style={{marginBottom: 'auto'}} className="bidding-result">
                                {
                                    maxBid.userUsername === username ? (
                                        auctionDetails.endDate > moment().format() ?
                                            <span style={{color: "green"}}>{t('text.winning')}</span> :
                                            <span style={{color: "green"}}>{t('text.won')}</span>
                                    ) : (
                                        auctionDetails.endDate > moment().format() ?
                                            <span style={{color: "red"}}>{t('text.losing')}</span> :
                                            <span style={{color: "red"}}>{t('text.lost')}</span>
                                    )
                                }
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
                                        {auctionDetails.bids.sort((a, b) => a.price > b.price ? -1 : 1).map(bid => {
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

export default BiddingDetailsPage;