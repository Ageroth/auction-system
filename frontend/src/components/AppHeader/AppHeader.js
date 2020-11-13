import React from 'react';
import { Link } from 'react-router-dom';
import { Layout,Menu ,Dropdown ,Select } from 'antd';
import { useSelector, useDispatch } from 'react-redux'
import { logOut, changeCurrentRole } from "../../actions/userActions";
import { useTranslation } from 'react-i18next';
import { HomeOutlined } from "@ant-design/icons";
import 'antd/dist/antd.css';
import './AppHeader.css'

const Header = Layout.Header;

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
                {/* <Link to={`/my-profile`}>Twoje konto</Link> */}
                Twoje konto
            </Menu.Item>
            <Menu.Divider/>
            <Menu.Item className="dropdown-item" key="logout" >
                Wyloguj
            </Menu.Item>
        </Menu>
      );

    const handleRoleChange = (newRole) => {
        dispatch(changeCurrentRole(newRole));
    }

    return (
        <Header style={{ padding: '0 0' }}>
            {isLoggedIn ? (
               <Menu className="header-menu" theme="dark" mode="horizontal">
                    <Menu.Item className="menu-left-item" key="home">
                        <Link to={`/`}> <HomeOutlined/> </Link>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item" key="dropdown">
                        <Dropdown overlay={dropdownMenu}>
                            <a> {username} </a>
                        </Dropdown>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item" key="select" disabled="true">
                        <Select defaultValue={currentRole} style={{ width: 140 }} onChange={handleRoleChange}>
                            <Select.Option value="ADMINISTRATOR" disabled={!roles.includes("ADMINISTRATOR")}> {t('role.admin')} </Select.Option>
                            <Select.Option value="MODERATOR" disabled={!roles.includes("MODERATOR")}> {t('role.mod')} </Select.Option>
                            <Select.Option value="CLIENT"disabled={!roles.includes("CLIENT")}> {t('role.client')} </Select.Option>
                        </Select>
                    </Menu.Item>
                </Menu>
            ) : (
                <Menu className="header-menu" theme="dark" mode="horizontal">
                    <Menu.Item className="menu-left-item" key="home">
                        <Link to={`/`}> <HomeOutlined/> </Link>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item" key="login">
                        <Link to={`/login`}> {t('text.logIn')} </Link>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item" key="signup">
                        <Link to={`/signup`}> {t('text.signUp')} </Link>
                    </Menu.Item>
               </Menu>
            )}
        </Header>
    );
}

export default AppHeader;