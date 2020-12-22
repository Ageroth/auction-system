import React from 'react';
import {useSelector} from "react-redux";
import {Link} from 'react-router-dom';
import {Button, Image, Input, Space, Table} from 'antd';
import AppLayout from '../../../components/AppLayout'
import {useTranslation} from 'react-i18next';
import moment from "moment";
import {SearchOutlined} from '@ant-design/icons';
import 'antd/dist/antd.css'
import './BiddingTablePage.css'

const BiddingTablePage = (props) => {
    const {t} = useTranslation();
    const {auctions, pagination, isLoading} = props;
    const username = useSelector(state => state.user.username);

    const getColumnSearchProps = () => ({
        filterDropdown: ({setSelectedKeys, selectedKeys, confirm, clearFilters}) => (
            <div style={{padding: 8}}>
                <Input
                    placeholder={t('text.search')}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => handleSearch(selectedKeys, confirm)}
                    style={{width: 188, marginBottom: 8, display: 'block'}}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => handleSearch(selectedKeys, confirm)}
                        icon={<SearchOutlined/>}
                        size="small"
                        style={{width: 90}}
                    >
                        {t('text.search')}
                    </Button>
                    <Button onClick={() => handleReset(clearFilters)} size="small" style={{width: 90}}>
                        {t('text.reset')}
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: filtered => <SearchOutlined style={{color: filtered ? '#1890ff' : undefined}}/>,
    });

    const handleSearch = (selectedKeys, confirm) => {
        confirm();
    };

    const handleReset = (clearFilters) => {
        clearFilters();
    };

    const columns = [
        {
            className: 'image-column',
            width: '10%',
            title: t('auctionLabels.itemImage'),
            dataIndex: 'itemImage',
            key: 'itemImage',
            render: itemImage => <Image width={200} src={"data:image/png;base64," + itemImage}
                                        alt={itemImage}/>
        },
        {
            width: '11%',
            title: t('auctionLabels.itemName'),
            dataIndex: 'itemName',
            key: 'itemName',
            ...getColumnSearchProps(),
            render: (text, record) => <Link style={{color: "#1890ff"}}
                                            to={`/my_biddings/${record.id}`}>{record.itemName}</Link>
        },
        {
            width: '7%',
            title: t('text.seller'),
            dataIndex: 'userUsername',
            key: 'userUsername',
            render: userUsername => userUsername === username ?
                <span style={{fontWeight: "bold"}}>{t('text.you')}</span> : userUsername
        },
        {
            title: t('auctionLabels.itemDescription'),
            dataIndex: 'itemDescription',
            key: 'itemDescription'
        },
        {
            width: '8%',
            title: t('auctionLabels.currentPrice'),
            dataIndex: 'currentPrice',
            key: 'currentPrice',
            render: (text, record) => record.currentPrice ? `${record.currentPrice} PLN` : `${record.startingPrice} PLN`
        },
        {
            width: '8%',
            title: t('auctionLabels.bidsNumber'),
            dataIndex: 'bidsNumber',
            key: 'bidsNumber'
        },
        {
            width: '10%',
            title: t('auctionLabels.startDate'),
            dataIndex: 'startDate',
            key: 'startDate',
            sorter: true,
            defaultSortOrder: 'ascend',
            render: startDate => `${moment(startDate).format('DD-MM-YYYY, HH:mm')}`
        },
        {
            width: '10%',
            title: t('auctionLabels.endDate'),
            dataIndex: 'endDate',
            key: 'endDate',
            sorter: true,
            render: endDate => `${moment(endDate).format('DD-MM-YYYY, HH:mm')}`
        },
        {
            width: '5%',
            title: t('auctionLabels.status'),
            dataIndex: 'status',
            key: 'status',
            filters: [
                {text: t('auctionLabels.current'), value: 'CURRENT'},
                {text: t('auctionLabels.finished'), value: 'FINISHED'}
            ],
            filterMultiple: false,
            defaultFilteredValue: ['CURRENT'],
            render: (text, record) => record.endDate > moment().format() ? t('auctionLabels.current') : t('auctionLabels.finished')
        }
    ];

    const handleTableChange = (pagination, filters, sorter) => {
        let auctionStatus;
        let searchQuery;

        filters.status ? auctionStatus = filters.status[0] : auctionStatus = null;
        filters.itemName ? searchQuery = filters.itemName[0] : searchQuery = null;

        props.handleTableChange({
            pagination,
            sortField: sorter.field,
            order: sorter.order,
            status: auctionStatus,
            query: searchQuery
        })
    };

    return (
        <AppLayout>
            <div className="bidding-table-page-wrapper">
                <Table
                    columns={columns}
                    rowKey={record => record.id}
                    dataSource={auctions}
                    pagination={pagination}
                    loading={isLoading}
                    onChange={handleTableChange}
                    bordered
                />
            </div>
        </AppLayout>
    );
}

export default BiddingTablePage;