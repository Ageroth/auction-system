import React from 'react';
import { Link } from 'react-router-dom';
import { Layout,Menu ,Dropdown ,Select } from 'antd';
import { useSelector, useDispatch } from 'react-redux'
import { logOut, changeCurrentRole } from '../../actions/userActions';
import { useTranslation } from 'react-i18next';
import { HomeOutlined } from '@ant-design/icons';
import allroles from '../../utils/allroles'
import 'antd/dist/antd.css';
import './AppHeader.css'

const Header = Layout.Header;
const { ADMINISTRATOR, MODERATOR, CLIENT } = allroles;

const AppHeader = () => {
    const isLoggedIn = useSelector(state => state.user.isLoggedIn);
    const username = useSelector(state => state.user.username);
    const roles = useSelector(state => state.user.roles);
    const currentRole = useSelector(state => state.user.currentRole);
    const {t} = useTranslation();
    const dispatch = useDispatch();

    const handleDropdownMenuClick = ({key}) => {
        if (key === "logout") {
            dispatch(logOut());
        }
    }

    const dropdownMenu = (
        <Menu onClick={handleDropdownMenuClick}>
            <Menu.Item className="dropdown-item" key="profile" >
                <Link to={`/my_profile`}> {t('text.yourAccount')} </Link>
            </Menu.Item>
            <Menu.Divider/>
            <Menu.Item className="dropdown-item" key="logout" >
                {t('text.logOut')}
            </Menu.Item>
        </Menu>
    );

    const handleRoleChange = (newRole) => {
        dispatch(changeCurrentRole(newRole));
    }

    return (
        <Header className="app-header" style={{ padding: '0 0' }}>
            {isLoggedIn ? (
               <Menu className="header-menu" theme="dark" mode="horizontal">
                    <Menu.Item className="menu-left-item" key="home">
                        <Link className="menu-link" to={`/`}> <HomeOutlined/> </Link>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item" key="dropdown">
                        <Dropdown classname="dropdown" overlay={dropdownMenu}>
                            <p style={{ color: "white" }}> {username} </p>
                        </Dropdown>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item" key="select" disabled="true">
                        <Select defaultValue={currentRole} style={{ width: 140 }} onChange={handleRoleChange}>
                            {roles.includes(ADMINISTRATOR) ? <Select.Option value={ADMINISTRATOR}> {t('role.admin')} </Select.Option> : null}
                            {roles.includes(MODERATOR) ? <Select.Option value={MODERATOR}> {t('role.mod')} </Select.Option> : null}
                            {roles.includes(CLIENT) ? <Select.Option value={CLIENT}> {t('role.client')} </Select.Option> : null}
                        </Select>
                    </Menu.Item>
                </Menu>
            ) : (
                <Menu className="header-menu" theme="dark" mode="horizontal">
                    <Menu.Item className="menu-left-item" key="home">
                        <Link className="menu-link" to={`/`}> <HomeOutlined/> </Link>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item" key="login">
                        <Link className="menu-link" to={`/login`}> {t('text.logIn')} </Link>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item" key="signup">
                        <Link className="menu-link" to={`/signup`}> {t('text.signUp')} </Link>
                    </Menu.Item>
               </Menu>
            )}
        </Header>
    );
}

export default AppHeader;