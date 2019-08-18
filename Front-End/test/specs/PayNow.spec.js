
import { createLocalVue, shallowMount } from '@vue/test-utils'
import Vuex from 'vuex'

import Profile from '@/components/paynow'
import {GlobalWithFetchMock} from "jest-fetch-mock";


import VueRouter from 'vue-router';
import { getHeapStatistics } from 'v8';


let wrapper
let store
let actions
let mutations
let state
let router = new VueRouter()
const localVue = createLocalVue()
localVue.use(VueRouter)
localVue.use(Vuex)
localVue.filter('dateFormatter',jest.fn())
localVue.filter('thousandSeparators',jest.fn())

let dataPayNow = {
    lastPayment: 1,
    nomorRekening: ' 0123456',
    namaAdmin: ' Admin fake',
    regularPayment: 10000
  }

store = new Vuex.Store({
    modules:{
        getters: jest.fn(),
        actions: jest.fn(),
        state: jest.fn(),
        mutations: jest.fn(),
    }
  })

beforeEach(()  => {
    fetch.resetMocks()
    window.alert=jest.fn()
    const expectedResponse =  ['a','b','c'] 
    fetch.mockResponseOnce(JSON.stringify(expectedResponse))
    wrapper = shallowMount(Profile, {
        // stubs:['HeaderSection','SidebarComponent'],
        propsData:{
            dataPayNow
        },
        store,
        localVue,   
        router, 
    });
})
afterEach(() => {
    jest.clearAllMocks()
    wrapper.destroy()
})
describe('Profile.vue',() =>{
    test('setup correctly', ()=>{
        expect(wrapper.text()).toContain('Pay Now')
        expect(wrapper.isVueInstance).toBeTruthy()
    })
    test('getMember called', ()=>{
        // wrapper.setData({
        //     myEmail:'a'
        // })
        wrapper.vm.myEmail = 'a'
        const expectedResponse =  ['a','b','c'] 
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        wrapper.vm.getMember()
        expect(wrapper.vm.groupMemberList.length).toBe(3)
    })
    test('setUntukMemberLain called', ()=>{
        expect(wrapper.vm.untukMemberLain).toBe(false)
        expect(wrapper.contains('#untukMemberLain')).toBe(true)
        const checkBox = wrapper.find('#untukMemberLain')
        checkBox.trigger('click')
        expect(wrapper.vm.untukMemberLain).toBe(true)
    })
    test('validateinput name empty', ()=>{
        const mockData = {
            periode: '',
            nomorRekeningPengirim:'',
            namaPengirim:'',
            buktiTransfer: null,
            tanggalTransfer:'',
            untukMemberLain:false,
            emailMemberLain:''
        }
        wrapper.setData(mockData)    
        wrapper.vm.validateInput()
        expect(window.alert).toBeCalled()
        expect(wrapper.vm.validateInput()).toBe(false)   
    })
    test('validateinput nomorRekening empty', ()=>{
        const mockData = {
            periode: 1,
            nomorRekeningPengirim:'',
            namaPengirim:'',
            buktiTransfer: null,
            tanggalTransfer:'',
            untukMemberLain:false,
            emailMemberLain:''
        }
        wrapper.setData(mockData)    
        wrapper.vm.validateInput()
        expect(window.alert).toBeCalled()

        expect(wrapper.vm.validateInput()).toBe(false)   
    })
    test('validateinput nama pengirim empty', ()=>{
        const mockData = {
            periode: 1,
            nomorRekeningPengirim:'123456789',
            namaPengirim:'',
            buktiTransfer: null,
            tanggalTransfer:'',
            untukMemberLain:false,
            emailMemberLain:''
        }
        wrapper.setData(mockData)    
        wrapper.vm.validateInput()
        expect(window.alert).toBeCalled()

        expect(wrapper.vm.validateInput()).toBe(false)   
    })
    test('validateinput buktitransfer null', ()=>{
        const mockData = {
            periode: 1,
            nomorRekeningPengirim:'123456789',
            namaPengirim:'Hahaha',
            buktiTransfer: null,
            tanggalTransfer:'',
            untukMemberLain:false,
            emailMemberLain:''
        }
        wrapper.setData(mockData)    
        wrapper.vm.validateInput()
        expect(window.alert).toBeCalled()

        expect(wrapper.vm.validateInput()).toBe(false)   
    })
    test('validateinput TanggalTransfer empty', ()=>{
        const mockData = {
            periode: 1,
            nomorRekeningPengirim:'123456789',
            namaPengirim:'Hahaha',
            buktiTransfer: '123456',
            tanggalTransfer:null,
            untukMemberLain:false,
            emailMemberLain:''
        }
        wrapper.setData(mockData)    
        wrapper.vm.validateInput()
        expect(window.alert).toBeCalled()

        expect(wrapper.vm.validateInput()).toBe(false)   
    })
    test('validateinput untukMemberLain but empty emailMemberLain', ()=>{
        const mockData = {
            periode: 1,
            nomorRekeningPengirim:'123456789',
            namaPengirim:'Hahaha',
            buktiTransfer: '123456',
            tanggalTransfer:123456789123,
            untukMemberLain:true,
            emailMemberLain:''
        }
        wrapper.setData(mockData)    
        wrapper.vm.validateInput()
        expect(window.alert).toBeCalled()

        expect(wrapper.vm.validateInput()).toBe(false)   
    })
    test('validateinput succeed', ()=>{
        const mockData = {
            periode: 1,
            nomorRekeningPengirim:'123456789',
            namaPengirim:'Hahaha',
            buktiTransfer: '123456',
            tanggalTransfer:123456789123,
            untukMemberLain:true,
            emailMemberLain:'hahaha@haha.com'
        }
        wrapper.setData(mockData)    
        wrapper.vm.validateInput()
        expect(wrapper.vm.validateInput()).toBe(true)   
    })
    
})

