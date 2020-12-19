import React, {useState} from 'react';
import {Button, Form, Input, InputNumber, Popconfirm, Spin} from 'antd'
import AppLayout from '../../components/AppLayout';
import {useTranslation} from 'react-i18next';
import 'antd/dist/antd.css';
import './AuctionEditPage.css'

const {TextArea} = Input;

const AuctionEditPage = (props) => {
    const [visible, setVisible] = useState(false);
    const [form] = Form.useForm();
    const {t} = useTranslation();
    const isSubmitting = props.isSubmitting;
    const auctionDetails = props.auctionDetails;

    const onFinish = (values) => {
        const payload = Object.assign({}, values);
        props.onSubmit(payload);
    }

    const showPopconfirm = () => {
        setVisible(true);
    }

    const handleCancel = () => {
        setVisible(false);
    }

    const handleOk = () => {
        setVisible(false);
        form.submit();
    }

    return (
        <AppLayout>
            {auctionDetails ? (
                <div className="auction-edit-page-wrapper">
                    <h1 style={{fontWeight: "bold"}}> {t('pageName.auctionEdit')} </h1>
                    <Form
                        form={form}
                        layout="vertical"
                        name="auction_edit_form"
                        className="auction-edit-form"
                        onFinish={onFinish}
                        scrollToFirstError
                    >
                        <Form.Item
                            label={t('auctionLabels.itemName')}
                            name="itemName"
                            initialValue={auctionDetails.itemName}
                            rules={[
                                {
                                    required: true,
                                    message: t('validation.required')
                                },
                                {
                                    max: 32,
                                    message: t('validation.max32chars')
                                }
                            ]}
                        >
                            <Input/>
                        </Form.Item>

                        <Form.Item
                            label={t('auctionLabels.itemDescription')}
                            name="itemDescription"
                            initialValue={auctionDetails.itemDescription}
                            rules={[
                                {
                                    required: true,
                                    message: t('validation.required')
                                },
                                {
                                    max: 4096,
                                    message: t('validation.max4096chars')
                                }
                            ]}
                        >
                            <TextArea/>
                        </Form.Item>

                        <Form.Item
                            label={t('auctionLabels.startingPrice')}
                            name="startingPrice"
                            initialValue={auctionDetails.startingPrice}
                            rules={[
                                {
                                    required: true,
                                    message: t('validation.required')
                                }
                            ]}
                        >
                            <InputNumber step={0.01} min={0.01} formatter={value => `${value} PLN`}
                                         parser={value => value.replace(/PL|PN|LN|P|N|L|\s?|(,*)/g, '')}/>
                        </Form.Item>

                        <Form.Item style={{marginBottom: "0"}}>
                            <Popconfirm
                                title={t('text.areYouSure')}
                                visible={visible}
                                onConfirm={handleOk}
                                onCancel={handleCancel}
                                okText={t('text.yes')}
                                cancelText={t('text.no')}
                            >
                                <Button type="primary" className="auction-edit-form-button" loading={isSubmitting}
                                        onClick={showPopconfirm}> {t('text.edit')} </Button>
                            </Popconfirm>
                        </Form.Item>
                    </Form>
                </div>
            ) : (
                <Spin size="large"/>
            )}
        </AppLayout>
    );
}

export default AuctionEditPage;