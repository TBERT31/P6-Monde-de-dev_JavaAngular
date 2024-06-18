export interface Article {
    id: number;
    title: string;
    content: string;
    author: string;
    topic_title: string;
    comments: number[];
    createdAt?: Date;
    updatedAt?: Date;
}