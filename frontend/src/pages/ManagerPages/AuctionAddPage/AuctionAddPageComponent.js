import React, {useState} from 'react'
import {Button, Checkbox, DatePicker, Form, Input, InputNumber, Popconfirm, Select, Upload} from 'antd'
import moment from 'moment';
import {useTranslation} from 'react-i18next';
import {LoadingOutlined, PlusOutlined} from '@ant-design/icons';
import AppLayout from "../../../components/AppLayout";
import 'antd/dist/antd.css'
import './AuctionAddPage.css'

const {TextArea} = Input;

function getBase64(img, callback) {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result));
    reader.readAsDataURL(img);
}

function disabledDate(current) {
    return current && current < moment().startOf("day")
}

const AuctionAddPage = (props) => {
    const [form] = Form.useForm();
    const {t} = useTranslation();
    const isSubmitting = props.isSubmitting;
    const [isLoading, setIsLoading] = useState(false);
    const [imageUrl, setImageUrl] = useState();
    const [scheduleItemVisible, setScheduleItemVisible] = useState(false);
    const [visible, setVisible] = useState(false);

    const handleChange = (info) => {
        if (info.file.status === 'uploading') {
            setIsLoading(true);
            return;
        }

        if (info.file.status === 'done') {
            getBase64(info.file.originFileObj, imageUrl => {
                    setImageUrl(imageUrl);
                    setIsLoading(false);
                }
            );
        }
    };

    const dummyRequest = ({file, onSuccess}) => {
        setTimeout(() => {
            onSuccess("ok");
        }, 0);
    };

    const uploadButton = (
        <div>
            {isLoading ? <LoadingOutlined/> : <PlusOutlined/>}
            <div style={{marginTop: 8}}>Upload</div>
        </div>
    );

    const scheduleItem = (
        <>
            <Form.Item
                label={t('auctionLabels.startDate')}
                name="startDate"
                rules={[
                    {
                        validator(rule, value) {
                            if (value) {
                                if (value < new Date()) return Promise.reject(t('validation.invalidDate'));
                                else return Promise.resolve('');
                            } else return Promise.reject(t('validation.required'));
                        }
                    }
                ]}
            >
                <DatePicker
                    format="DD/MM/YYYY HH:mm"
                    disabledDate={disabledDate}
                    showTime={{format: 'HH:mm'}}
                />
            </Form.Item>
        </>
    )

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

    const onFinish = (values) => {
        const formData = new FormData();

        formData.append('file', values.itemImage.file.originFileObj);
        formData.append('auction', new Blob([JSON.stringify({
            "itemName": values.itemName,
            "itemDescription": values.itemDescription,
            "startingPrice": values.startingPrice,
            "startDate": values.startDate,
            "duration": values.duration
        })], {
            type: "application/json"
        }));
        props.onSubmit(formData);
    }

    return (
        <AppLayout>
            <div className="auction-add-page-wrapper">
                <h1 style={{fontWeight: "bold"}}> {t('pageName.auctionAdd')} </h1>
                <Form
                    form={form}
                    layout="horizontal"
                    name="auction_add_form"
                    className="auction-add-form"
                    onFinish={onFinish}
                    scrollToFirstError
                >
                    <Form.Item
                        valuePropName='file'
                        label={t('auctionLabels.itemImage')}
                        name="itemImage"
                        rules={[
                            {
                                required: true,
                                message: t('validation.required')
                            }
                        ]}
                    >
                        <Upload
                            listType="picture-card"
                            className="avatar-uploader"
                            showUploadList={false}
                            onChange={handleChange}
                            customRequest={dummyRequest}
                        >
                            {imageUrl ? <img src={imageUrl} alt="avatar" style={{width: '100%'}}/> : uploadButton}
                        </Upload>
                    </Form.Item>

                    <Form.Item
                        label={t('auctionLabels.itemName')}
                        name="itemName"
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
                        initialValue={0.01}
                        rules={[
                            {
                                required: true,
                                message: t('validation.required')
                            }
                        ]}
                    >
                        <InputNumber step={0.01} precision={2} min={0.01} formatter={value => `${value} PLN`}
                                     parser={value => value.replace(/PL|PN|LN|P|N|L|\s?|(,*)/g, '')}/>
                    </Form.Item>

                    <Checkbox onChange={e => setScheduleItemVisible(e.target.checked)}>
                        {t('text.schedule')}
                    </Checkbox>

                    {scheduleItemVisible ? scheduleItem : null}

                    <Form.Item
                        label={t('auctionLabels.duration')}
                        name="duration"
                        initialValue={3}
                        rules={[
                            {
                                required: true,
                                message: t('validation.required')
                            }
                        ]}
                    >
                        <Select style={{width: 120}}>
                            <Select.Option value={1}>1</Select.Option>
                            <Select.Option value={2}>2</Select.Option>
                            <Select.Option value={3}>3</Select.Option>
                            <Select.Option value={4}>4</Select.Option>
                            <Select.Option value={5}>5</Select.Option>
                            <Select.Option value={6}>6</Select.Option>
                            <Select.Option value={7}>7</Select.Option>
                        </Select>
                    </Form.Item>

                    <Form.Item style={{marginBottom: '0'}}>
                        <Popconfirm
                            title={t('text.areYouSure')}
                            visible={visible}
                            onConfirm={handleOk}
                            onCancel={handleCancel}
                            okText={t('text.yes')}
                            cancelText={t('text.no')}
                        >
                            <Button type="primary" className="auction-add-form-button" loading={isSubmitting}
                                    onClick={showPopconfirm} disabled={isLoading}> {t('text.add')} </Button>
                        </Popconfirm>
                    </Form.Item>
                </Form>
            </div>
        </AppLayout>
    );
}

export default AuctionAddPage;