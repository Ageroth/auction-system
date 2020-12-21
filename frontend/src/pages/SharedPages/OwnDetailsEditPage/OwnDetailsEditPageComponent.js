import React, {useState} from 'react';
import {Button, Form, Input, Popconfirm, Spin} from 'antd'
import AppLayout from '../../../components/AppLayout';
import {useTranslation} from 'react-i18next';
import 'antd/dist/antd.css';
import './OwnDetailsEditPage.css'

const OwnDetailsEditPage = (props) => {
    const [visible, setVisible] = useState(false);
    const [form] = Form.useForm();
    const {t} = useTranslation();
    const isSubmitting = props.isSubmitting;
    const myDetails = props.myDetails;

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
        form.submit();
        setVisible(false);
    }

    return (
        <AppLayout>
            {myDetails ? (
                <div className="own-details-edit-page-wrapper">
                    <h1 style={{fontWeight: "bold"}}> {t('pageName.edit')} </h1>
                    <Form
                        form={form}
                        layout="vertical"
                        name="own_details_edit_form"
                        className="own-details-edit-form"
                        onFinish={onFinish}
                        scrollToFirstError
                    >
                        <Form.Item
                            label={t('userLabels.firstName')}
                            name="firstName"
                            initialValue={myDetails.firstName}
                            rules={[
                                {
                                    required: true,
                                    message: t('validation.required')
                                },
                                {
                                    pattern: new RegExp("^[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$"),
                                    message: t('validation.regex.name')
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
                            label={t('userLabels.lastName')}
                            name="lastName"
                            initialValue={myDetails.lastName}
                            rules={[
                                {
                                    required: true,
                                    message: t('validation.required')
                                },
                                {
                                    pattern: new RegExp("^[a-zA-ZąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$"),
                                    message: t('validation.regex.name')
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
                            label={t('userLabels.phoneNumber')}
                            name="phoneNumber"
                            initialValue={myDetails.phoneNumber}
                            rules={[
                                {
                                    required: true,
                                    message: t('validation.required')
                                },
                                {
                                    pattern: new RegExp("^[0-9]{9,10}$"),
                                    message: t('validation.regex.phoneNumber')
                                }
                            ]}
                        >
                            <Input/>
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
                                <Button type="primary" className="own-details-edit-form-button" loading={isSubmitting}
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

export default OwnDetailsEditPage;