import React from 'react'
import { Link } from 'react-router-dom';
import { Table, Tag } from 'antd';
import { useTranslation } from 'react-i18next';
import AppLayout from '../../components/AppLayout'
import allroles from '../../utils/allroles'
import 'antd/dist/antd.css'
import './UserListPage.css'

const { ADMINISTRATOR, MODERATOR } = allroles;

const UserListPage = (props) => {
    const {t} = useTranslation();

    const formatDate = (string) => {
        return new Date(string).toLocaleString([]);
    }

    const columns = [
        {
            title: t('userLabels.name'),
            dataIndex: 'lastName',
            key: 'lastName',
            sorter: true,
            render: (text, record) => <Link style={{ color: "#1890ff" }} to={`/users/${record.id}`}> {record.firstName} {record.lastName} </Link> ,
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
            title: t('userLabels.roles'),
            key: 'userAccessLevelNames',
            dataIndex: 'userAccessLevelNames',
            render: userAccessLevelNames => (
                <>
                    {userAccessLevelNames.map(userAccessLevelName => {
                        let color;
                        let value;

                        if (userAccessLevelName === ADMINISTRATOR) {
                            color = 'volcano';
                            value = t('role.admin');
                        }
                        else if (userAccessLevelName === MODERATOR) {
                            color = 'green';
                            value = t('role.mod');
                        }
                        else {
                            color = 'geekblue';
                            value =  t('role.client');
                        }

                        return (
                            <Tag color={color} key={userAccessLevelName}>
                                {value.toUpperCase()}
                            </Tag>
                        );
                    })}
                </>
            ),
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
                bordered
            />
        </AppLayout>
    );
}

export default UserListPage;