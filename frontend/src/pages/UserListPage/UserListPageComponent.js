import React, { useState } from 'react'
import { Table, Input, Button, Space } from 'antd';
import { useTranslation } from 'react-i18next';
import Highlighter from 'react-highlight-words';
import AppLayout from '../../components/AppLayout'
import { SearchOutlined } from '@ant-design/icons';
import 'antd/dist/antd.css'
import './UserListPage.css'

const UserListPage = (props) => {
    // const {t} = useTranslation();
    // const [searchText, setSearchText] = useState('');
    // const [searchedColumn, setSearchedColumn] = useState('');
    // const [searchInput, setSearchInput] = useState('');
 
    // const getColumnSearchProps = dataIndex => ({
    //     filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
    //       <div style={{ padding: 8 }}>
    //         <Input
    //           ref={node => {
    //             setSearchInput(node);
    //           }}
    //           placeholder={`Search ${dataIndex}`}
    //           value={selectedKeys[0]}
    //           onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
    //           onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
    //           style={{ width: 188, marginBottom: 8, display: 'block' }}
    //         />
    //         <Space>
    //           <Button
    //             type="primary"
    //             onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
    //             icon={<SearchOutlined />}
    //             size="small"
    //             style={{ width: 90 }}
    //           >
    //             Search
    //           </Button>
    //           <Button onClick={() => handleReset(clearFilters)} size="small" style={{ width: 90 }}>
    //             Reset
    //           </Button>
    //         </Space>
    //       </div>
    //     ),
    //     filterIcon: filtered => <SearchOutlined style={{ color: filtered ? '#1890ff' : undefined }} />,
    //     onFilter: (value, record) =>
    //       record[dataIndex]
    //         ? record[dataIndex].toString().toLowerCase().includes(value.toLowerCase())
    //         : '',
    //     onFilterDropdownVisibleChange: visible => {
    //       if (visible) {
    //         setTimeout(() => searchInput, 100);
    //       }
    //     },
    //     render: text =>
    //       searchedColumn === dataIndex ? (
    //         <Highlighter
    //           highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
    //           searchWords={[searchText]}
    //           autoEscape
    //           textToHighlight={text ? text.toString() : ''}
    //         />
    //       ) : (
    //         text
    //       ),
    //   });
    
    // const handleSearch = (selectedKeys, confirm, dataIndex) => {
    //     confirm();
    //     setSearchText(selectedKeys[0]);
    //     setSearchedColumn(dataIndex);
    //   };
    
    // const handleReset = clearFilters => {
    //     clearFilters();
    //     setSearchText('');
    //   };
    
    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            sorter: true,
            render: (text, record) => <a href={'/users/' + record.id}> {record.firstName} {record.lastName} </a>,
            width: '20%'
        },
        {
            title: 'Username',
            dataIndex: 'username',
            key: 'username',
            width: '20%'
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: 'Activated',
            dataIndex: 'activated',
            key: 'activated',
            filters: [
                { text: 'yes', value: true },
                { text: 'no', value: false }
            ],
            render: activated => activated ? 'yes' : 'no',
            width: '20%'
        }
    ];
    
    return (
        <Table
            columns={columns}
            dataSource={props.data}
            pagination={props.pagination}
            loading={props.isLoading}
        />
    );
}

export default UserListPage;