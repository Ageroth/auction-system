import React from 'react';
import {Link, useHistory} from 'react-router-dom';
import {Button, Image, Input, Space, Table} from 'antd';
import AppLayout from '../../components/AppLayout'
import {useTranslation} from 'react-i18next';
import moment from "moment";
import {SearchOutlined} from '@ant-design/icons';
import 'antd/dist/antd.css'
import './AuctionTablePage.css'

const AuctionTablePage = (props) => {
    const {t} = useTranslation();
    const history = useHistory();

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

    const handleAdd = () => {
        const currentLocation = history.location.pathname;

        history.push(`${currentLocation}/add`);
    }

    const columns = [
        {
            title: t('auctionLabels.itemImage'),
            dataIndex: 'itemImage',
            key: 'itemImage',
            render: itemImage => <Image width={200} src={"data:image/png;base64," + itemImage}
                                        alt={itemImage}/>
        },
        {
            title: t('auctionLabels.itemName'),
            dataIndex: 'itemName',
            key: 'itemName',
            ...getColumnSearchProps(),
            render: (text, record) => <Link style={{color: "#1890ff"}}
                                            to={`/auctions/${record.id}`}>{record.itemName}</Link>
        },
        {
            title: t('auctionLabels.itemDescription'),
            dataIndex: 'itemDescription',
            key: 'itemDescription'
        },
        {
            title: t('auctionLabels.currentPrice'),
            dataIndex: 'currentPrice',
            key: 'currentPrice',
            render: (text, record) => record.currentPrice ? `${record.currentPrice} PLN` : `${record.startingPrice} PLN`
        },
        {
            title: t('auctionLabels.bidsNumber'),
            dataIndex: 'bidsNumber',
            key: 'bidsNumber'
        },
        {
            title: t('auctionLabels.startDate'),
            dataIndex: 'startDate',
            key: 'startDate',
            sorter: true,
            defaultSortOrder: 'ascend',
            render: startDate => `${moment(startDate).format('DD-MM-YYYY HH:mm')}`
        },
        {
            title: t('auctionLabels.endDate'),
            dataIndex: 'endDate',
            key: 'endDate',
            sorter: true,
            render: endDate => `${moment(endDate).format('DD-MM-YYYY HH:mm')}`
        },
        {
            title: t('auctionLabels.status'),
            dataIndex: 'status',
            key: 'status',
            filters: [
                {text: 'Current', value: 'CURRENT'},
                {text: 'Finished', value: 'FINISHED'}
            ],
            filterMultiple: false,
            defaultFilteredValue: 'CURRENT',
            render: (text, record) => record.endDate > moment().format() ? 'Current' : 'Finished'
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
            <div className="auction-table-wrapper">
                <Table
                    columns={columns}
                    rowKey={record => record.id}
                    dataSource={props.auctions}
                    pagination={props.pagination}
                    loading={props.isLoading}
                    onChange={handleTableChange}
                    bordered
                />
                <Button className="auction-table-button" type="primary" onClick={handleAdd}> {t('text.add')} </Button>
            </div>
        </AppLayout>
    );
}

export default AuctionTablePage;