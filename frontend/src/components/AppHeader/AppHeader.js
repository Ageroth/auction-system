import React from 'react';
import {Link} from 'react-router-dom';
import {Layout,Menu,Dropdown} from 'antd';
import {useSelector,useDispatch} from 'react-redux'
import {LOGOUT_USER} from "../../actions/types";
import {useTranslation} from 'react-i18next';
import {HomeOutlined} from "@ant-design/icons";
import 'antd/dist/antd.css';
import './AppHeader.css'

const Header = Layout.Header;

const AppHeader = () => {
    const isLogged = useSelector(state => state.user.isLogged);
    const username = useSelector(state => state.user.username);
    const roles = useSelector(state => state.user.roles);
    const currentRole = useSelector(state => state.user.currentRole);
    const {t} = useTranslation();
    const dispatch = useDispatch()

    const handleMenuClick = ({key}) => {
        if (key === "logout") {
            dispatch({ type: LOGOUT_USER })
        }
    }

    const dropdownMenu = (
        <Menu onClick={handleMenuClick}>
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
      

    return (
        <Header style={{ padding: '0 0' }}>
            {isLogged ? (
               <Menu className="header-menu" theme="dark" mode="horizontal">
                    <Menu.Item className="menu-left-item" key="home">
                        <Link to={`/`}> <HomeOutlined/> </Link>
                    </Menu.Item>
                    <Menu.Item className="menu-right-item">
                    <Dropdown overlay={dropdownMenu}>
                        <a> {username} </a>
                    </Dropdown>
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