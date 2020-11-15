import React, { useState } from 'react'
import { Table, Input, Button, Space } from 'antd';
import { useTranslation } from 'react-i18next';
import AppLayout from '../../components/AppLayout'
import { SearchOutlined } from '@ant-design/icons';
import 'antd/dist/antd.css'
import './UserListPage.css'

const UserListPage = (props) => {
    const {t} = useTranslation();

    const formatDate = (string) => {
        return new Date(string).toLocaleString([]);
    }

    const columns = [
        {
            title: 'Name',
            dataIndex: 'lastName',
            key: 'lastName',
            sorter: true,
            render: (text, record) => <a href={'/users/' + record.id}> {record.firstName} {record.lastName} </a>,
        },
        {
            title: t('userLabels.username'),
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: t('userLabels.email'),
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: t('userLabels.created'),
            dataIndex: 'createdAt',
            key: 'createdAt',
            sorter: true,
            render: createdAt => `${formatDate(createdAt)}`
        },
        {
            title: t('userLabels.activated'),
            dataIndex: 'activated',
            key: 'activated',
            filters: [
                { text: t('text.yes'), value: true },
                { text: t('text.no'), value: false }
            ],
            filterMultiple: false,
            render: activated => activated ? t('text.yes') : t('text.no'),
        }
    ];

    const handleTableChange = (pagination, filters, sorter, searchText) => {
        props.handleTableChange({
            sortField: sorter.field,
            order: sorter.order,
            query: searchText,
            pagination,
            ...filters
        })
      };
    
    return (
        <AppLayout>
            <Table
                className="user-table"
                columns={columns}
                rowKey={record => record.id}
                dataSource={props.data}
                pagination={props.pagination}
                loading={props.isLoading}
                onChange={handleTableChange}
            />
        </AppLayout>
    );
}

export default UserListPage;