import React from 'react';
import {Link, useHistory} from 'react-router-dom';
import {Button, Input, Space, Table, Tag} from 'antd';
import moment from 'moment';
import AppLayout from '../../../components/AppLayout'
import {useTranslation} from 'react-i18next';
import allroles from '../../../utils/allroles'
import {SearchOutlined} from '@ant-design/icons';
import 'antd/dist/antd.css'
import './UserTablePage.css'

const {ADMINISTRATOR, MANAGER, CLIENT} = allroles;

const UserTablePage = (props) => {
    const {t} = useTranslation();
    const history = useHistory();
    const {users, pagination, isLoading} = props;

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
            title: t('userLabels.name'),
            dataIndex: 'lastName',
            key: 'lastName',
            sorter: true,
            ...getColumnSearchProps(),
            render: (text, record) => <Link style={{color: "#1890ff"}}
                                            to={`/users/${record.id}`}> {record.firstName} {record.lastName} </Link>
        },
        {
            title: t('userLabels.username'),
            dataIndex: 'username',
            key: 'username'
        },
        {
            title: t('userLabels.email'),
            dataIndex: 'email',
            key: 'email'
        },
        {
            title: t('userLabels.created'),
            dataIndex: 'createdAt',
            key: 'createdAt',
            sorter: true,
            render: createdAt => `${moment(createdAt).format('DD-MM-YYYY, HH:mm')}`
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
                        } else if (userAccessLevelName === MANAGER) {
                            color = 'green';
                            value = t('role.man');
                        } else if (userAccessLevelName === CLIENT) {
                            color = 'geekblue';
                            value = t('role.client');
                        }

                        return (
                            <Tag color={color} key={userAccessLevelName}>
                                {value.toUpperCase()}
                            </Tag>
                        );
                    })}
                </>
            )
        },
        {
            title: t('userLabels.activated'),
            dataIndex: 'activated',
            key: 'activated',
            filters: [
                {text: t('text.yes'), value: true},
                {text: t('text.no'), value: false}
            ],
            filterMultiple: false,
            render: activated => activated ? t('text.yes') : t('text.no')
        }
    ];

    const handleTableChange = (pagination, filters, sorter) => {
        let activationStatus;
        let searchQuery;

        filters.activated ? activationStatus = filters.activated[0] : activationStatus = null;
        filters.lastName ? searchQuery = filters.lastName[0] : searchQuery = null;

        props.handleTableChange({
            pagination,
            sortField: sorter.field,
            order: sorter.order,
            status: activationStatus,
            query: searchQuery
        })
    };

    return (
        <AppLayout>
            <div className="user-table-page-wrapper">
                <Table
                    columns={columns}
                    rowKey={record => record.id}
                    dataSource={users}
                    pagination={pagination}
                    loading={isLoading}
                    onChange={handleTableChange}
                    bordered
                />
                <Button className="user-table-button" type="primary" onClick={handleAdd}> {t('text.add')} </Button>
            </div>
        </AppLayout>
    );
}

export default UserTablePage;