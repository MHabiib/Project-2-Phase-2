
import { createLocalVue, shallowMount } from '@vue/test-utils'
import Vuex from 'vuex'

import Profile from '@/page/profile'
import {GlobalWithFetchMock} from "jest-fetch-mock";

// import mockAxios from '../../__mocks__/axios';

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

store = new Vuex.Store({
    modules:{
        getters: jest.fn(),
        actions: jest.fn(),
        state: jest.fn(),
        mutations: jest.fn(),
    }
  })

beforeEach(()  => {
    const mockSuccessResponse = '200 OK'
          
    const mockFetchPromise = Promise.resolve({
        json: () => Promise.resolve(mockSuccessResponse),
      });
      // memalsukan fungsi fetch API
      // selalu mengembalikan nilai sesuai yang diinginkan
    global.fetch = jest.fn().mockResolvedValue(mockFetchPromise)
    wrapper = shallowMount(Profile, {
        stubs:['HeaderSection','SidebarComponent'],
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

        expect(wrapper.text()).toContain('Profile')
        expect(wrapper.isVueInstance).toBeTruthy()
    })
    // test('function getUserData  called',async (done) =>{

    // })
})

