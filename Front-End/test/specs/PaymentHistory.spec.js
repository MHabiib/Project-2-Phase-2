
import { createLocalVue, shallowMount } from '@vue/test-utils'
import Vuex from 'vuex'
import PaymentHistory from '@/page/PaymentHistory'
import {GlobalWithFetchMock} from "jest-fetch-mock";
import VueRouter from 'vue-router';
import { V4MAPPED } from 'dns';


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
    fetch.resetMocks()
    window.alert=jest.fn()
    const expectedResponse =  ['a','b','c'] 
    fetch.mockResponseOnce(JSON.stringify(expectedResponse))
    wrapper = shallowMount(PaymentHistory, {
        stubs:['HeaderSection','SidebarComponent'],
        mocks:{
        },
        store,
        localVue,   
        router,
        attachToDocument: true
    });
})
afterEach(() => {
    jest.clearAllMocks()
    wrapper.destroy()
})
describe('PaymentHistory.vue',() =>{
    test('setup correctly', ()=>{
        const querySelectorSpy = jest.spyOn(document, 'querySelector')

        wrapper.vm.scroll()
        expect(querySelectorSpy).toHaveBeenCalledTimes(1)
        expect(wrapper.text()).toContain('Payment Table')
        expect(wrapper.isVueInstance).toBeTruthy()
    })


})

