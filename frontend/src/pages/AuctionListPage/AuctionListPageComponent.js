import React from 'react';
import {Table} from 'antd';
import AppLayout from '../../components/AppLayout'
import {useTranslation} from 'react-i18next';
import 'antd/dist/antd.css'
import './AuctionListPage.css'
import moment from "moment";

const AuctionListPage = (props) => {
    const {t} = useTranslation();

    const columns = [
        {
            title: 'Name',
            dataIndex: 'itemName',
            key: 'itemName'
        },
        {
            title: 'Description',
            dataIndex: 'itemDescription',
            key: 'itemDescription'
        },
        {
            title: 'Seller',
            dataIndex: 'userUsername',
            key: 'userUsername'
        },
        {
            title: 'Current price',
            dataIndex: 'price',
            key: 'price',
            render: (text, record) => record.currentPrice ? `${record.currentPrice} PLN` : `${record.startingPrice} PLN`
        },
        {
            title: 'Start date',
            dataIndex: 'startDate',
            key: 'startDate',
            render: startDate => `${moment(startDate).format('DD-MM-YYYY HH:mm')}`
        },
        {
            title: 'End date',
            dataIndex: 'endDate',
            key: 'endDate',
            render: endDate => `${moment(endDate).format('DD-MM-YYYY HH:mm')}`
        },
        {
            title: 'Bids number',
            dataIndex: 'bidsNumber',
            key: 'bidsNumber'
        }
    ];

    const handleTableChange = (pagination, filters, sorter) => {
        props.handleTableChange({
            sortField: sorter.field,
            order: sorter.order,
            pagination,
            ...filters
        })
    };

    return (
        <AppLayout>
            <div className="user-list-wrapper">
                <Table
                    columns={columns}
                    rowKey={record => record.id}
                    dataSource={props.data}
                    pagination={props.pagination}
                    loading={props.isLoading}
                    onChange={handleTableChange}
                    bordered
                />
            </div>
        </AppLayout>
    );
}

export default AuctionListPage;