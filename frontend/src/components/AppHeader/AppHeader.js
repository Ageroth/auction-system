import React from 'react';
import {Link} from 'react-router-dom';
import {Avatar, Dropdown, Layout, Menu, Select} from 'antd';
import {useDispatch, useSelector} from 'react-redux'
import {changeCurrentRole, logOut} from '../../actions/userActions';
import {useTranslation} from 'react-i18next';
import {BarsOutlined, BookOutlined, HomeOutlined, PlusOutlined, TeamOutlined, UserAddOutlined} from '@ant-design/icons';
import allroles from '../../utils/allroles'
import 'antd/dist/antd.css';
import './AppHeader.css'

const Header = Layout.Header;
const {ADMINISTRATOR, MANAGER, CLIENT} = allroles;

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
            <Menu.Item className="dropdown-item" key="profile">
                <Link to={`/my_profile`}> {t('navbarLink.myAccount')} </Link>
            </Menu.Item>
            <Menu.Divider/>
            <Menu.Item className="dropdown-item" key="logout">
                {t('text.logOut')}
            </Menu.Item>
        </Menu>
    )

    const handleRoleChange = (newRole) => {
        dispatch(changeCurrentRole(newRole));
    }

    const navigationBar = () => {
        let items = null;
        switch (currentRole) {
            case ADMINISTRATOR:
                items = (
                    <>
                        <Menu.Item className="menu-left-item" key="users" icon={<TeamOutlined/>}>
                            <Link className="menu-link" to={`/users`}> {t('navbarLink.users')} </Link>
                        </Menu.Item>
                        <Menu.Item className="menu-left-item" key="user-add" icon={<UserAddOutlined/>}>
                            <Link className="menu-link" to={`/users/add`}> {t('navbarLink.newUser')} </Link>
                        </Menu.Item>
                    </>
                )
                break;

            case MANAGER:
                items = (
                    <>
                        <Menu.Item className="menu-left-item" key="auctions" icon={<BookOutlined/>}>
                            <Link className="menu-link" to={`/auctions`}> {t('navbarLink.auctions')} </Link>
                        </Menu.Item>
                        <Menu.Item className="menu-left-item" key="user-add" icon={<PlusOutlined/>}>
                            <Link className="menu-link"
                                  to={`/my_auctions/add`}> {t('navbarLink.newAuction')} </Link>
                        </Menu.Item>
                        <Menu.Item className="menu-left-item" key="my_auctions" icon={<BarsOutlined/>}>
                            <Link className="menu-link"
                                  to={`/my_auctions`}> {t('navbarLink.myAuctions')} </Link>
                        </Menu.Item>
                    </>
                )
                break;

            case CLIENT:
                items = (
                    <>
                        <Menu.Item className="menu-left-item" key="auctions" icon={<BookOutlined/>}>
                            <Link className="menu-link" to={`/auctions`}> {t('navbarLink.auctions')} </Link>
                        </Menu.Item>
                        <Menu.Item className="menu-left-item" key="my_biddings" icon={<BarsOutlined/>}>
                            <Link className="menu-link" to={`/my_biddings`}> {t('navbarLink.myBiddings')} </Link>
                        </Menu.Item>
                    </>
                )
                break;

            default:
                break;
        }

        return (
            <>
                {items}
            </>
        );
    }

    return (
        <Header className="app-header" style={{padding: '0 0'}}>
            {isLoggedIn ? (
                <Menu className="header-menu" theme="dark" mode="horizontal">
                    <Menu.Item className="menu-left-item" key="home">
                        <Link className="menu-link" to={`/`}> <HomeOutlined/> </Link>
                    </Menu.Item>
                    {navigationBar()}
                    <Menu.Item className="menu-right-item" key="dropdown">
                        <Dropdown classname="dropdown" overlay={dropdownMenu}>
                            <Avatar
                                style={{
                                    color: '#001529',
                                    backgroundColor: "white",
                                }}
                            >
                                {username.toUpperCase()}
                            </Avatar>
                        </Dropdown>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item" key="select" disabled="true">
                        <Select defaultValue={currentRole} style={{width: 140}} onChange={handleRoleChange}>
                            {roles.includes(ADMINISTRATOR) ?
                                <Select.Option value={ADMINISTRATOR}> {t('role.admin')} </Select.Option> : null}
                            {roles.includes(MANAGER) ?
                                <Select.Option value={MANAGER}> {t('role.man')} </Select.Option> : null}
                            {roles.includes(CLIENT) ?
                                <Select.Option value={CLIENT}> {t('role.client')} </Select.Option> : null}
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