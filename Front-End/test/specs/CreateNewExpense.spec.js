
import { createLocalVue, mount } from '@vue/test-utils'
import Vuex from 'vuex'
import CreateNewExpense from '@/components/createNewExpense'
import {GlobalWithFetchMock} from "jest-fetch-mock";
import VueRouter from 'vue-router';


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


store = new Vuex.Store({
    modules:{
        getters: jest.fn(),
        actions: jest.fn(),
        state: jest.fn(),
        mutations: jest.fn(),
    }
  })

beforeEach(()  => {
    // fetch.resetMocks()
    window.alert=jest.fn()
    // const expectedResponse =  ['a','b','c'] 
    // fetch.mockResponseOnce(JSON.stringify(expectedResponse))

    wrapper = mount(CreateNewExpense, {
        // stubs:['HeaderSection','SidebarComponent'],

        mocks:{
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
        expect(wrapper.text()).toContain('Request Expense')
        expect(wrapper.isVueInstance).toBeTruthy()
    })
    test('validateInput failed no biaya', ()=>{
        wrapper.setData({
            biaya: '',
            namaPengeluaran: '',
            deskripsiPengeluaran: ''
        })
        expect(wrapper.vm.validateInput()).toBe(false)
        expect(window.alert).toBeCalled()
    })
    test('validateInput failed no namaPengeluaran', ()=>{
        wrapper.setData({
            biaya: '1000',
            namaPengeluaran: '',
            deskripsiPengeluaran: ''
        })
        expect(wrapper.vm.validateInput()).toBe(false)
        expect(window.alert).toBeCalled()
    })
    test('validateInput failed no deskripsiPengeluaran', ()=>{
        wrapper.setData({
            biaya: '1000',
            namaPengeluaran: 'test',
            deskripsiPengeluaran: ''
        })
        expect(wrapper.vm.validateInput()).toBe(false)
        expect(window.alert).toBeCalled()
    })
    test('validateInputsucceed', ()=>{
        wrapper.setData({
            biaya: '1000',
            namaPengeluaran: 'test',
            deskripsiPengeluaran: 'testing1234567890'
        })
        expect(wrapper.vm.validateInput()).toBe(true)
        expect(window.alert).not.toBeCalled()
    })
    test('checkchar called', ()=>{
        wrapper.vm.checkChar = jest.fn()
        wrapper.vm.checkChange= jest.fn()
        const input= wrapper.find('.price')
        input.setValue(123)
        input.trigger('keypress')
        input.trigger('change')
        expect(wrapper.vm.biaya).toBe('123')     
        expect(wrapper.vm.checkChar).toBeCalled()
        expect(wrapper.vm.checkChange).toBeCalled()
    })
    test('check change called', ()=>{
        wrapper.vm.checkChar = jest.fn()
        wrapper.vm.checkChange= jest.fn()
        const input= wrapper.find('.price')
        input.setValue(123)
        input.trigger('keypress')
        input.trigger('change')
        expect(wrapper.vm.biaya).toBe('123')     
        expect(wrapper.vm.checkChar).toBeCalled()
        expect(wrapper.vm.checkChange).toBeCalled()
    })
    test('thousandseparators called',()=>{
        wrapper.vm.thousandSeparators = jest.fn()
        const input= wrapper.find('.price')
        input.setValue(123456789)
        input.trigger('keypress')
        input.trigger('change')
        expect(wrapper.vm.thousandSeparators).toBeCalled()
    })
    test('event refreshData called',(done)=>{
        wrapper.setData({
            biaya: '1000',
            namaPengeluaran: 'test',
            deskripsiPengeluaran: 'testing1234567890'
        })
        // fetch.mockResponseOnce({ok:true})
        // wrapper.vm.createNewExpense = jest.fn()
        const res = {
            headers: {
                get: jest.fn()
            },
            ok:true
        }
        global.fetch = jest.fn().mockResolvedValue(res); 
        wrapper.vm.createNewExpense()
        wrapper.vm.$nextTick(() => {
            expect(wrapper.emitted().refreshData.length).toBe(1)            
            expect(wrapper.emitted().refreshData.length).toBe(1)
            done()
        })
    })
})

