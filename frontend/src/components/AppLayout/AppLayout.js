import React from 'react';
import { Layout } from 'antd';
import AppHeader from '../AppHeader'
import 'antd/dist/antd.css';
import './AppLayout.css'


const { Footer } = Layout;

const AppLayout = ({children}) => {
    return (
        <Layout className="app-layout">
            <AppHeader/>
            <div className="app-content">
                {children}
            </div>
            <Footer className="app-footer"> Auction System ©2020 </Footer>
        </Layout>
    );
}

export default AppLayout;